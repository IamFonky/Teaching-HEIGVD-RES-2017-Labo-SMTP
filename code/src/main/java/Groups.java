import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

/**
 * Created by Fonky on 05.04.2017.
 */
public class Groups
{
   private Group[] groups;

   public Groups(){}

   public Groups(Object[] groups)
   {
      this.groups = new Group[groups.length];
      for(int i = 0; i < groups.length; ++i)
      {
         this.groups[i] = (Group)groups[i];
      }
   }
}
