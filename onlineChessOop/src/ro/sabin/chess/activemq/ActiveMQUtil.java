/**
 *
 */
package ro.sabin.chess.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ro.sabin.chess.Chess;
import ro.sabin.chess.pieces.ChessConstants;
import ro.sabin.chess.ui.AppBoard;
import ro.sabin.chess.ui.InfoBoard;

/**
 * Functii utilitare pt comunicarea cu serverul ActiveMQ
 *
 * @author sabin
 *
 */
public class ActiveMQUtil {

  static final String user = "admin";
  static final String password = "admin";
  static final String host = "localhost";
  static final String port = "61616";
  static final String topicName = "sabin";

  public static final long CLIENT_ID = System.currentTimeMillis();

  static Session session;
  static Destination topic;
  static Connection connection;
  static MessageConsumer consumer;
  static MessageProducer producer;

  // conenct to ActiveMQ Server
  public static boolean connect(AppBoard myBoard, InfoBoard myInfoBoard) {
    try {
      System.out.println("connecting....");

      ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);

      connection = factory.createConnection(user, password);
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      topic = new ActiveMQTopic(topicName);

      consumer = session.createConsumer(topic);
      consumer.setMessageListener(new MessageListener() {

        @Override
        public void onMessage(Message msg) {
          try {
            System.out.println("msg received: " + msg);
            long clientId = msg.getLongProperty("CLIENT");
            if (clientId == CLIENT_ID) {
              // ignore self message
              System.out.println("self msg, ignore...");
              return;
            }

            // .. process message
            int type = msg.getIntProperty("TYPE");
            switch (type) {
              case ChessConstants.TYPE_START_OK:
                Chess.getDisplay().syncExec(new Runnable() {

                  @Override
                  public void run() {
                    InfoBoard infoBoard = Chess.getInfoBoard();
                    infoBoard.addItem("Jocul a inceput... va rugam asteptati mutarea primului jucator");
                  }
                });

                break;
              case ChessConstants.TYPE_START_INIT:
                // am primit un request de start de la cineva
                // trebuie sa raspuns cu OK_START
                System.out.println("primit START req...");
                // ar trebui sa trimit start ok ca sa inceapa jocul!
                if (!send("Raspuns Cerere Start", ChessConstants.TYPE_START_OK, "")) {
                  //TODO: sa dau eroare aici in interfata!
                  MessageBox mb = new MessageBox(myBoard.getShell(), SWT.ICON_ERROR);
                  mb.setMessage("Eroare la raspuns cerere start");
                  mb.open();
                } else {
                  Chess.getDisplay().syncExec(new Runnable() {

                    @Override
                    public void run() {
                      myInfoBoard.addItem("Jocul a inceput, puteti muta");
                    }
                  });
                  myBoard.setMyTurn(true);
                }
                break;

              case ChessConstants.TYPE_MOVE:
                myBoard.setMyTurn(true);
                // am prmit start joc ok de la cineva, incepem jocul
                System.out.println("Mutare");
                String coor = msg.getStringProperty("coor");
                int xstart = Integer.parseInt(coor.charAt(0) + "");
                int ystart = Integer.parseInt(coor.charAt(1) + "");
                int xstop = Integer.parseInt(coor.charAt(2) + "");
                int ystop = Integer.parseInt(coor.charAt(3) + "");
                System.out.println(myBoard);
                myBoard.appBoard.move(xstart, ystart, xstop, ystop);
                Chess.getDisplay().syncExec(new Runnable() {

                  @Override
                  public void run() {
                    myBoard.udpateTableMove(xstart, ystart, xstop, ystop);
                    myInfoBoard
                      .addItem("Mutare adversar -- " + xstart + " : " + ystart + " -> " + xstop + " : " + ystop);
                  }
                });

                break;
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });

      producer = session.createProducer(topic);
      producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    } catch (JMSException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  // send message
  public static boolean send(String body, int type, String coor) {
    try {
      if (session == null) {
        return false;
      }
      System.out.println("sending....");
// "START"
      TextMessage msg = session.createTextMessage(body);
      msg.setIntProperty("TYPE", type);
      msg.setLongProperty("CLIENT", CLIENT_ID);
      msg.setStringProperty("coor", coor);

      Chess.getDisplay().syncExec(new Runnable() {

        @Override
        public void run() {
          if (type == ChessConstants.TYPE_START_INIT) {
            InfoBoard infoBoard = Chess.getInfoBoard();
            infoBoard.addItem("Cererea de start a fost trimisa");
          }
//else if (type == ChessConstants.TYPE_START_OK) {
//            InfoBoard infoBoard = Chess.getInfoBoard();
//            infoBoard.addItem("Jocul a inceput... va rugam asteptati mutarea primului jucator");
//          }
        }
      });

      producer.send(msg);
      System.out.println("Sent message!");
    } catch (JMSException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  //disconnect to ActiveMQ Server
  public static boolean disconnect() {
    try {
      System.out.println("disconnecting....");

      connection.close();
    } catch (JMSException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
