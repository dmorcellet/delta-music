package delta.music.files;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.framework.objects.data.DataProxy;
import delta.common.utils.files.TextFileReader;
import delta.common.utils.text.EndOfLine;
import delta.music.Album;
import delta.music.Interpret;
import delta.music.MusicDataSource;
import delta.music.Song;

/**
 * Parses .lp files to populate the music database.
 * @author DAM
 */
public class LPFileParser
{
  private static final Logger LOGGER=LoggerFactory.getLogger(LPFileParser.class);

  /**
   * Constructor.
   */
  public LPFileParser()
  {
    // Nothing to do !!
  }

  /**
   * Parse the given file and populate the music database.
   * @param pathName File to read.
   */
  public void parseFile(File pathName)
  {
    MusicDataSource source=MusicDataSource.getInstance();
    TextFileReader fp=new TextFileReader(pathName);
    fp.start();

    String line=null;

    // Tokens
    String albumToken="#ALBUM(";
    String interpreteToken="#INTERPRETE";
    String imageToken="#IMAGE(";
    String begin="#DEBUT_CHANSON();";
    String titreChanson="#TITRE(";
    String end="#FIN_CHANSON();";

    // LP title
    String lpTitle=null;
    // Interpret
    String interpretName=null;
    // Image
    String image=null;

    while((line=fp.getNextLine())!=null)
    {
      if (line.equals(begin)) break;
      else if(line.startsWith(albumToken))
      {
        StringTokenizer st=new StringTokenizer(line, "\"");
        st.nextToken();
        lpTitle=st.nextToken();
        LOGGER.debug("LP name ["+lpTitle+"]");
      }
      else if(line.startsWith(interpreteToken))
      {
        StringTokenizer st=new StringTokenizer(line, "\"");
        st.nextToken();
        interpretName=st.nextToken();
        LOGGER.debug("Interpret name ["+interpretName+"]");
      }
      else if(line.startsWith(imageToken))
      {
        StringTokenizer st=new StringTokenizer(line, "\"");
        st.nextToken();
        image=st.nextToken();
        LOGGER.debug("Image ["+image+"]");
      }
    }
    if (lpTitle==null)
    {
      fp.terminate();
      return;
    }

    Album a=new Album(null);
    a.setTitle(lpTitle);
    List<Interpret> iList=source.getInterpretDataSource().loadObjectSet(Interpret.NAME_SET,new Object[]{interpretName});
    if ((iList!=null) && (!iList.isEmpty()))
    {
      Interpret interpret=iList.get(0);
      a.setInterpretProxy(new DataProxy<Interpret>(interpret.getPrimaryKey(),source.getInterpretDataSource()));
    }
    else
    {
      Interpret interpret=new Interpret(null);
      interpret.setName(interpretName);
      source.getInterpretDataSource().create(interpret);
      a.setInterpretProxy(new DataProxy<Interpret>(interpret.getPrimaryKey(),source.getInterpretDataSource()));
    }
    a.setImage(image);
    source.getAlbumDataSource().create(a);

    // Songs
    String currentSong=null;
    StringBuilder text=new StringBuilder();

    while((line=fp.getNextLine())!=null)
    {
      if(line.equals(begin))
      {
        currentSong=null;
        text.setLength(0);
      }
      else if(line.startsWith(titreChanson))
      {
        StringTokenizer st=new StringTokenizer(line, "\"");
        st.nextToken();
        currentSong=st.nextToken();
      }
      else if(line.equals(end))
      {
        if(currentSong!=null)
        {
          Song song=new Song(null);
          song.setName(currentSong);
          song.setText(text.toString());
          song.setAlbumProxy(new DataProxy<Album>(a.getPrimaryKey(),source.getAlbumDataSource()));
          source.getSongDataSource().create(song);
          currentSong=null;
          text.setLength(0);
        }
      }
      else if(line.equals(imageToken))
      {
        StringTokenizer st=new StringTokenizer(line, "\"");
        st.nextToken();
        image=st.nextToken();
        LOGGER.debug("LP image ["+image+"]");
      }
      else if(!line.startsWith("#"))
      {
        if(currentSong!=null)
        {
          text.append(line);
          text.append(EndOfLine.NATIVE_EOL);
        }
      }
    }

    fp.terminate();
  }

  /**
   * Main method for this class.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    MusicDataSource.getInstance();
    LPFileParser parser=new LPFileParser();
    File f=new File("d:\\dada\\export\\musique\\src\\lp\\abbey_road.lp");
    parser.parseFile(f);
    MusicDataSource.getInstance().close();
  }
}
