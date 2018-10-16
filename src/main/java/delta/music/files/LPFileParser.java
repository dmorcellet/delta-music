package delta.music.files;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

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

    String line_l=null;

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

    while((line_l=fp.getNextLine())!=null)
    {
      if (line_l.equals(begin)) break;
      else if(line_l.startsWith(albumToken))
      {
        StringTokenizer st=new StringTokenizer(line_l, "\"");
        st.nextToken();
        lpTitle=st.nextToken();
        System.out.println("LP name ["+lpTitle+"]");
      }
      else if(line_l.startsWith(interpreteToken))
      {
        StringTokenizer st=new StringTokenizer(line_l, "\"");
        st.nextToken();
        interpretName=st.nextToken();
        System.out.println("Interpret name ["+interpretName+"]");
      }
      else if(line_l.startsWith(imageToken))
      {
        StringTokenizer st=new StringTokenizer(line_l, "\"");
        st.nextToken();
        image=st.nextToken();
        System.out.println("Image ["+image+"]");
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
    if ((iList!=null) && (iList.size()>0))
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
    StringBuffer text_l=new StringBuffer();

    while((line_l=fp.getNextLine())!=null)
    {
      if(line_l.equals(begin))
      {
        currentSong=null;
        text_l.setLength(0);
      }
      else if(line_l.startsWith(titreChanson))
      {
        StringTokenizer st=new StringTokenizer(line_l, "\"");
        st.nextToken();
        currentSong=st.nextToken();
      }
      else if(line_l.equals(end))
      {
        if(currentSong!=null)
        {
          Song song=new Song(null);
          song.setName(currentSong);
          song.setText(text_l.toString());
          song.setAlbumProxy(new DataProxy<Album>(a.getPrimaryKey(),source.getAlbumDataSource()));
          source.getSongDataSource().create(song);
          currentSong=null;
          text_l.setLength(0);
        }
      }
      else if(line_l.equals(imageToken))
      {
        StringTokenizer st=new StringTokenizer(line_l, "\"");
        st.nextToken();
        image=st.nextToken();
        System.out.println("LP image ["+image+"]");
      }
      else if(!line_l.startsWith("#"))
      {
        if(currentSong!=null)
        {
          text_l.append(line_l);
          text_l.append(EndOfLine.NATIVE_EOL);
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
