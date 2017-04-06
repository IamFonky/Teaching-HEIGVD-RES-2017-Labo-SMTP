package ch.heig.fonkygati;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Program that play a "Prank Campain" and send pranks emails from senders victims to receivers victims. It uses files
 * that contain series of predefined prank messages and list of victims.
 *
 * @author Pierre-Benjamin Monaco (IamFonky)
 * @author Gaëtan Othenin-Girard (GOthGir)
 */
public class OurSmtpClient
{
   // Tokens used in the logs to specify who sent the displayed message
   private final static String CLIENT_P = "Client  ~~o ";
   private final static String SERVER_P = "Serveur ~~o ";

   // Predefined separator used in the victims and pranks files to indicate the end of a data
   private final static String END_OF_DATA_LOL = "!$&#%#&$!";

   // Predefined separator used to say to the server that we have finished to send datas
   private final static String END_OF_DATA = "\r\n.\r\n";

   // Informations about the SMTP server we want to connect
   private static String name = "fonkygati";
   private static String host = "localhost";
   private static int port = 2525;

   // HashSets containing the groups of victims and the prank texts
   private static HashSet<Group> groups;
   private static HashSet<String> pranks;

   // Socket connected to the SMTP server
   private static Socket socket;

   // Reader and Writer used to communicate with the SMTP server
   private static SmtpWriter writer;
   private static BufferedReader reader;

   // Random generator used to chose a random prank
   private static Random rand;


   /**
    * Main method of the "Prank Campain" program. It accept two arguments :
    *    -victims "victimsFile"
    *    -pranks "pranksFile"
    * See the README.md for the specification of the files.
    * @param args victims and pranks files.
    */
   public static void main(String[] args)
   {
      // Check for args
      if(args.length < 1)
      {
         System.out.println("Le programme à besoin d'arguments pour démarrer." +
                 "Pour plus d'infos lancez le avec la commande -?");
         return;
      }

      // We collect each commands and arguments and do the corresponding action
      for (int i = 0; i < args.length; ++i)
      {
         String argument = "";
         // Gets securely the argument of the actual command
         if(args.length > (i + 1))
         {
            argument = args[i + 1];
         }

         try
         {
            // We get the current command
            String command = args[i];

            // Executes the corresponding action or displays the help
            if (command.equals("-victims") && args.length > (i + 1))
            {
               // Fetches victims in victim file
               fetchVictims(argument);
               i++;
            } else if (command.equals("-pranks") && args.length > (i + 1))
            {
               // Fetches pranks in victim pranks
               fetchPranks(argument);
               i++;
            }
            else if(command.equals("-address") && args.length > (i + 1))
            {
               // Sets the host address
               host = args[i+1];
               i++;
            }
            else if(command.equals("-port") && args.length > (i + 1))
            {
               // Sets the port number
               port = Integer.valueOf(args[i+1]);
               i++;
            }
            else
            {
               // Displays help!
               System.out.println("Le programme possède quatres commandes (dans n'importe quel ordre) : ");
               System.out.println("-victim  (obligatoire)      : chemin vers le fichier contenant les groupes de victimes");
               System.out.println("-pranks  (obligatoire)      : chemin vers le fichier contenant les blagues");
               System.out.println("-address (defaut=localhost) : adresse IP ou URL du serveur SMTP");
               System.out.println("-port    (defaut=2525)      : port du serveur SMTP");
               System.out.println();
               return;
            }
         }
         catch (IOException e)
         {
            System.out.println("Le fichier " + argument + " n'existe pas ou " +
                  "ne peut pas être lu. \r\nErreur --> " + e.toString());
            return;
         }
      }

      // Connect to the SMTP server
      connect();
      // Send the prank messages
      groupSend();
      // Disconnect from the SMTP server
      disconnect();
   }

   /**
    * Method used to connect the client to the SMTP server.
    */
   private static void connect()
   {
      try
      {
         // Connection to the server.
         socket = new Socket(host, port);
      }
      catch (IOException e)
      {
         System.out.println("Il y a eu un souci à la connexion : " + e.toString());
         return;
      }

      try
      {
         // We create the writer to send messages to the server and a reader to get the response from the server
         writer = new SmtpWriter(new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))));
         reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      }
      catch (IOException e)
      {
         System.out.println("Il y a eu un souci à la création du reader et writer : "
                 + e.toString());
         return;
      }
   }

   /**
    * Method that takes a victim file and fetch the email addresses.
    * @param path path to the victim file.
    * @throws IOException throws an exception if the file doesn't exist.
    */
   private static void fetchVictims(String path) throws IOException
   {
      // Read the victim file
      reader = new BufferedReader(new FileReader(path));
      groups = new HashSet<>();
      String jsonGroupLine;
      String jsonGroup = "";

      // For each line from the file, collect the victims' email adresse
      while ((jsonGroupLine = reader.readLine()) != null)
      {
         // We clean the line by removing the end of data separator
         String[] cleanLine = jsonGroupLine.split(END_OF_DATA_LOL);
         if(cleanLine.length > 0)
         {
            jsonGroup += cleanLine[0];
         }

         // We parse que line and add the victims to the group
         if(jsonGroupLine.indexOf(END_OF_DATA_LOL) != -1)
         {
            groups.add(JsonObjectMapper.parseJson(jsonGroup, Group.class));
            jsonGroup = "";
         }
      }
      reader.close();
   }

   /**
    * Method that takes a prank file and fetch the prank texts.
    * @param path path to the prank file.
    * @throws IOException throws an exception if the file doesn't exist.
    */
   private static void fetchPranks(String path) throws IOException
   {
      // Read the prank file
      reader = new BufferedReader(new FileReader(path));
      pranks = new HashSet<>();
      String prankLine;
      String prank = "";

      // For each line, we get the prank text
      while ((prankLine = reader.readLine()) != null)
      {
         // We clean the line by removing the end of data separator
         String[] cleanLine = prankLine.split(END_OF_DATA_LOL);
         // If the line doesn't contain an end of data separator, we add a end of line separator to the prank text
         // Otherwise, the prank text is finished and we add the prank to the pranks list
         if(cleanLine.length > 0 && !cleanLine[0].equals(END_OF_DATA_LOL))
         {
            prank += cleanLine[0] + "\r\n";
         }
         else
         {
            pranks.add(prank);
            prank = "";
         }
      }
      reader.close();
   }

   /**
    * Method that send a message to a group of victims with a predefined subject (Let's LOL).
    */
   private static void groupSend()
   {
      groupSend("Let's LOL");
   }

   /**
    * Method that send a prank message to a group of victims with a given subject.
    * @param subject the message's subject.
    */
   private static void groupSend(String subject)
   {
      // Initalize random to choose random pranks
      rand = new Random();

      // For each group of victims, we send them a prank message from each sender victims chosen
      for (Group group : groups)
      {
         for (String sender : group.getSenders())
         {
            // Sand the mail with a random selected prank text
            sendMail(
                    sender,
                    subject,
                    (String)(pranks.toArray()[Math.abs(rand.nextInt()%pranks.size())]),
                    group.getReceivers()
            );
         }
      }
   }

   /**
    * Method that send a mail by sending the right commands to the server with the given informations.
    * @param emailFrom the sender's email address.
    * @param subject the subject of the mail.
    * @param message the content of the mail.
    * @param emailsTo the email addresses we want to send the message.
    */
   private static void sendMail(String emailFrom, String subject, String message, String... emailsTo)
   {
      // Getting the welcome message
      easyRead();

      // Sending the EHLO command
      sendAndFlush("EHLO " + name);

      // Getting the response (infos of the capacities)
      easyRead();

      // Sending the mail's source
      sendAndFlush("MAIL FROM: " + emailFrom);

      // Getting the response (ok)
      easyRead();

      for (String emailTo : emailsTo)
      {
         // Sending all the receivers
         sendAndFlush("RCPT TO: " + emailTo);
         // Getting the response (ok)
         easyRead();
      }

      // Sending message data request
      sendAndFlush("DATA");
      // Getting the response (start data)
      easyRead();

      // Sending the datas
      List<String> msgBody = makeHeader(emailFrom, subject, emailsTo);
      msgBody.add(message);
      sendAndFlush(msgBody.toArray(new String[0]));

      // Sending end of data command
      sendAndFlush(END_OF_DATA);
   }

   /**
    * Method used to make the header from the email.
    * @param emailFrom the sender's email address.
    * @param subject the subject of the mail.
    * @param emailsTo the receivers of the mail.
    * @return
    */
   private static List<String> makeHeader(String emailFrom, String subject,String... emailsTo)
   {
      List<String> header = new ArrayList<String>();
      // Add the sender
      header.add("From: " + emailFrom);
      // Add all the receivers
      for (int i = 0; i < emailsTo.length; ++i)
      {
         header.add((i == 0) ? "To: " : "Cc: " + emailsTo[i]);
      }
      // Add the mail's subject
      header.add("Subject: " + subject);
      header.add("");

      // Return the formated header for the mail
      return header;
   }

   /**
    * Method used to read the response from a SMTP server. It is make specifically for SMTP servers.
    */
   private static void easyRead()
   {
      try
      {
         String lastLine;
         // We read each line sent by the server while there isn't a space character in the 4th position
         // A message with a space means that there isn't a new line after the current one (specific to SMTP)
         do
         {
            lastLine = reader.readLine();
            System.out.println(SERVER_P + lastLine);
         }
         while (lastLine.length() > 3 && lastLine.charAt(3) != ' ');
      }
      catch (IOException e)
      {
         System.out.println("Erreur lors de l'écoute du serveur : " + e.toString());
      }
   }

   /**
    * Method used to write lines to the server.
    * @param lines strings sent as lines that we want to write to the server.
    */
   private static void sendAndFlush(String... lines)
   {
      // We send line by line
      for (String line : lines)
      {
         // Show the client message in the logs
         System.out.println(CLIENT_P + line);
         writer.println(line);

      }
      writer.flush();
   }

   /**
    * Method used to disconnect from the server.
    */
   private static void disconnect()
   {
      try
      {
         socket.close();
         reader.close();
      }
      catch (IOException e)
      {
         System.out.println("Une erreur s'est produite à la fermeture du programme : " +
                 e.toString());
      }
   }
}
