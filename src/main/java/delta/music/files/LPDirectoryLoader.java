package delta.music.files;

import java.io.File;

import delta.common.utils.files.iterator.AbstractFileIteratorCallback;
import delta.common.utils.files.iterator.FileIterator;
import delta.music.MusicDataSource;

public class LPDirectoryLoader extends AbstractFileIteratorCallback
{
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

  public static void main(String[] args)
  {
    MusicDataSource.getInstance();
    new LPDirectoryLoader();
    MusicDataSource.getInstance().close();
  }
}
