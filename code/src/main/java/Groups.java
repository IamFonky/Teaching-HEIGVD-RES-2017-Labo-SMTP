import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Fonky on 05.04.2017.
 */
public class Groups
{
   private Set<Group> groups;

   public Groups(){}

   public Groups(Set<Group> groups)
   {
       this.groups = groups;
   }

    public void addGroup(Group group)
    {
        this.groups.add(group);
    }
}
