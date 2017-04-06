import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fonky on 04.04.2017.
 */
public class Group
{

   private String[] senders;
   private String[] receivers;

   public Group() {}

   public Group(String[] senders, String[] receivers)
   {
      this.senders = senders;
      this.receivers = receivers;
   }

   public String[] getSenders()
   {
      return senders;
   }

   public void setSenders(String[] senders)
   {
      this.senders = senders;
   }


   public String[] getReceivers()
   {
      return receivers;
   }

   public void setReceivers(String[] receivers)
   {
      this.receivers = receivers;
   }
}
