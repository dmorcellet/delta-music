package delta.music;

import delta.common.framework.objects.data.DataObject;
import delta.common.framework.objects.data.ObjectSource;

public class Interpret extends DataObject<Interpret>
{
  public static final String CLASS_NAME="INTERPRET";
  // Sets
  public static final String NAME_SET="NAME";

  private String _name;

  @Override
  public String getClassName()
  {
    return CLASS_NAME;
  }

  public Interpret(Long primaryKey, ObjectSource<Interpret> source)
  {
    super(primaryKey,source);
  }

  public String getName()
  {
    return _name;
  }

  public void setName(String name)
  {
    _name=name;
  }
}
