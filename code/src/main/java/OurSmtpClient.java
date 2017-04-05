
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.*;


/**
 * Created by Fonky on 04.04.2017.
 */
public class OurSmtpClient
{
   private final static String CLIENT_P = "Client  ~~o ";
   private final static String SERVER_P = "Serveur ~~o ";

   private final static String END_OF_DATA = "\r\n.\r\n";

   private static String name = "Fonky_Gati";
   private static String host = "localhost";
   private static int port = 25;
   private static String emailFrom = "pierre-benjamin.monaco@heig-vd.ch";
//   private static String emailFrom = "miguel.santamaria@heig-vd.ch";
   private static String[] emailsTo = {"imfonky@gmail.com","gaetan.othenin-girard@heig-vd.ch"};
//   private static String[] emailsTo = {"olivier.liechti@heig-vd.ch"};
   private static String subject = "Tchô l'artiste!";
//   private static String subject = "[RES] SMTP - pierre-benjamin.monaco@heig-vd.ch";
   private static String data = "Yo GÂTEAU!!! TESTS";
//   private static String data = "Laboratoire réussi.";
   private static Groups groups;

   private static Socket socket;

   private static PrintWriter writer;
   private static BufferedReader reader;


   public static void main(String[] args)
   {
      for(int i = 0; i < args.length; ++i)
      {
         String command = args[i];
         if(command == "-victims" || args.length > (i + 1))
         {
            try
            {
               BufferedReader fr = new BufferedReader(new FileReader(args[i+1]));
               String jsonData = "";
               String jsonReader = "";
               while((jsonReader = fr.readLine()) != null)
               {
                  jsonData += jsonReader;
               };

               Group g1 = new Group(
                       new String[]
                               {
                                    "salut1@yoyo.com",
                                    "salut2@yoyo.com",
                                    "salut3@yoyo.com",
                                    "salut4@yoyo.com"
                               },
                       new String[]
                               {
                                       "ayeaye1@yoyo.com",
                                       "ayeaye2@yoyo.com",
                                       "ayeaye3@yoyo.com",
                                       "ayeaye4@yoyo.com",
                               }
               );

               ArrayList<Group> lsgrps = new ArrayList<Group>();
               lsgrps.add(g1);

               Groups grps = new Groups(lsgrps.toArray());

               JsonObjectMapper.toJson(grps);


//               groups = JsonObjectMapper.parseJson(jsonData, ArrayList.class);
            }
            catch (Exception e)
            {
               System.out.println("Le fichier" + args[i+1] + " n'existe pas ou " +
                       "ne peut pas être lu.");
               return;
            }

            i++;

         }
         else if(command == "-pranks" || args.length > (i + 1))
         {
            FileReader fileReader;
            try
            {
               fileReader = new FileReader(args[i+1]);
            }
            catch (FileNotFoundException e)
            {
               System.out.println("Le fichier" + args[i+1] + " n'existe pas.");
               return;
            }

            //TODO : implémenter aussi un gestionnaire pour récupérer les pranks
         }
         else
         {
            System.out.println("Le programme possède deux commandes : -victim et " +
                    "pranks, prenant chacune un chemin de fichier en paramêtre.");
            return;
         }
      }

      try
      {
         socket = new Socket(host,port);
      }
      catch (IOException e)
      {
         System.out.println("Il y a eu un souci à la connexion : " + e.toString());
         return;
      }

      try
      {
         writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
         reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      }
      catch (IOException e)
      {
         System.out.println("Il y a eu un souci à la création du reader et writer : "
                 + e.toString());
         return;
      }

      try
      {
//         for(int i = 0; i < groups.size(); ++i)
//         {
//            for(String sender : groups.get(i).getSenders())
//            {
//               sendMail(sender,"PRANK PRANK","BLABLABLA",groups.get(i).getReceivers());
//            }
//         }
      }
      catch (Exception e)
      {
         System.out.println("Le fichier des groupes est mal formaté : " + e.toString());
      }

      sendMail(emailFrom,subject,data,emailsTo);

   }

   private static void sendMail(String emailFrom, String subject, String message, String... emailsTo)
   {
      //Récupération de la ligne de bienvenue
      easyRead();

      //Envoi de la commande EHLO
      sendAndFlush("EHLO " + name);

      //Récupération de la réponse (infos sur les capacités)
      easyRead();

      //Envoi de la source du message
      sendAndFlush("MAIL FROM: " + emailFrom);

      //Récupération de la réponse (email from ok)
      easyRead();

      for(String emailTo : emailsTo)
      {
         //Envoi de chaque destinataire du message
         sendAndFlush("RCPT TO: " + emailTo);
         //Récupération de la réponse (email to ok)
         easyRead();
      }

      //Demande d'envoie du contenu du message
      sendAndFlush("DATA");
      //Récupération de la réponse (start data)
      easyRead();

      //Envoi des données
      List<String> msgBody = makeHeader();
      msgBody.add(message);
      sendAndFlush(msgBody.toArray(new String[0]));

      //Envoi de la commande de fin de données
      sendAndFlush(END_OF_DATA);
   }

   private static List<String> makeHeader()
   {
      List<String> header = new ArrayList<String>();
      header.add("From: " + emailFrom);
      for(int i = 0; i < emailsTo.length; ++i)
      {
         header.add((i == 0) ? "To: " : "Cc: " + emailsTo[i]);
      }
      header.add("Subject: " + subject);
      header.add("");
      return header;
   }

   private static void easyRead()
   {
      try
      {
         String lastLine;
         do
         {
            lastLine = reader.readLine();
            System.out.println(SERVER_P + lastLine);
         }
         while(lastLine.length() > 3 && lastLine.charAt(3) != ' ');
      }
      catch (IOException e)
      {
         System.out.println("Erreur lors de l'écoute du serveur : " + e.toString() );
      }
   }

   private static void sendAndFlush(String... lines)
   {
      for (String line : lines)
      {
         System.out.println(CLIENT_P + line);
         writer.println(line);
      }
      writer.flush();
   }

}
