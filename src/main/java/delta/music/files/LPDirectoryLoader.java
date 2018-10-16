package delta.music.files;

import java.io.File;

import delta.common.utils.files.iterator.AbstractFileIteratorCallback;
import delta.common.utils.files.iterator.FileIterator;
import delta.music.MusicDataSource;

/**
 * Loader for a whole LP directory.
 * <p>
 * Loads all the .lp files of a directory to populate the music database.
 * @author DAM
 */
public class LPDirectoryLoader extends AbstractFileIteratorCallback
{
  /**
   * Constructor.
   */
  public LPDirectoryLoader()
  {
    FileIterator it=new FileIterator(new File("d:\\dada\\export\\musique\\src\\lp"), false, this);
    it.run();
  }

  @Override
  public void handleFile(File absolute, File relative)
  {
    String name=absolute.getAbsolutePath();
    if(name.endsWith(".lp"))
    {
      LPFileParser parser=new LPFileParser();
      parser.parseFile(absolute);
    }
  }

  /**
   * Main method for this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    MusicDataSource.getInstance();
    new LPDirectoryLoader();
    MusicDataSource.getInstance().close();
  }
}
