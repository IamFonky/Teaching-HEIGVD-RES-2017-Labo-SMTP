import java.io.PrintWriter;

/**
 * Created by Fonky on 06.04.2017.
 */
public class SmtpWriter
{
   static private final String END_OF_SMTP_CMD = "\r\n";
   private PrintWriter pw;

   public SmtpWriter(PrintWriter pw)
   {
      this.pw = pw;
   }

   public void println(String toPrint)
   {
      pw.print(toPrint + END_OF_SMTP_CMD);
   }

   public void flush()
   {
      pw.flush();
   }
}
