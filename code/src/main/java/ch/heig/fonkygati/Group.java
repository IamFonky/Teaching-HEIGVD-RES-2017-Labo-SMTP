package ch.heig.fonkygati;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a group of victims used in the "Prank Compain". It contains victims senders and receivers.
 */
public class Group
{
   // Lists of senders and receivers victims
   private String[] senders;
   private String[] receivers;

   /**
    * Default constructor for Group.
    */
   public Group() {}

   /**
    * Construct a Group with given senders and receivers victims as strings.
    * @param senders array of senders victims as strings.
    * @param receivers array of receivers victims as strings.
    */
   public Group(String[] senders, String[] receivers)
   {
      this.senders = senders;
      this.receivers = receivers;
   }

   /**
    * Give the list of senders victims.
    * @return array of strings representing the emails of the senders.
    */
   public String[] getSenders()
   {
      return senders;
   }

   /**
    * Set the list of senders victims.
    * @param senders array of strings representing the emails of the senders.
    */
   public void setSenders(String[] senders)
   {
      this.senders = senders;
   }

   /**
    * Give the list of receivers victims.
    * @return array of strings representing emails of the receivers.
    */
   public String[] getReceivers()
   {
      return receivers;
   }

   /**
    * Set the list of receivers victims.
    * @param receivers array of strings representing emails of the receivers.
    */
   public void setReceivers(String[] receivers)
   {
      this.receivers = receivers;
   }
}
