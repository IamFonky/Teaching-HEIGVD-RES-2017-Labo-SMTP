package ch.heig.fonkygati;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Class representing a writer for a SMTP server.
 *
 * @author Pierre-Benjamin Monaco (IamFonky)
 * @author Gaëtan Othenin-Girard (GOthGir)
 */
public class SmtpWriter
{
   // The end of command marker used by the SMTP protocol is "\r\n"
   static private final String END_OF_SMTP_CMD = "\r\n";

   // Writer used to communicate with the SMTP server
   private PrintWriter pw;

   /**
    * SmtpWriter constructor with a given writer.
    * @param pw writer used to communicate with the SMTP server.
    */
   public SmtpWriter(PrintWriter pw)
   {
      this.pw = pw;
   }

   /**
    * Write a line to the SMTP server.
    * @param toPrint line that we want to send to the SMTP server.
    */
   public void println(String toPrint)
   {
      pw.print(toPrint + END_OF_SMTP_CMD);
   }

   /**
    * Flush after a write.
    */
   public void flush()
   {
      pw.flush();
   }
}
