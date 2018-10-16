package delta.music;

import delta.common.framework.objects.data.DataObject;

/**
 * Interpret.
 * @author DAM
 */
public class Interpret extends DataObject<Interpret>
{
  /**
   * Class name.
   */
  public static final String CLASS_NAME="INTERPRET";
  // Sets
  /**
   * Key for the 'name' set: set of interprets with a given name.
   */
  public static final String NAME_SET="NAME";

  private String _name;

  @Override
  public String getClassName()
  {
    return CLASS_NAME;
  }

  /**
   * Constructor.
   * @param primaryKey Primary key.
   */
  public Interpret(Long primaryKey)
  {
    super();
    setPrimaryKey(primaryKey);
  }

  /**
   * Get the name of this interpret.
   * @return a name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this interpret.
   * @param name Name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }
}
