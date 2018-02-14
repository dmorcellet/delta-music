package delta.music;

import delta.common.framework.objects.data.DataObject;

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

  public Interpret(Long primaryKey)
  {
    super();
    setPrimaryKey(primaryKey);
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
