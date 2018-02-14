package delta.music.web.pages;

import java.io.PrintWriter;
import java.util.List;

import delta.common.framework.objects.data.ObjectsManager;
import delta.common.framework.web.WebPage;
import delta.common.framework.web.WebPageTools;
import delta.common.utils.ParameterFinder;
import delta.music.Album;
import delta.music.Interpret;
import delta.music.MusicDataSource;
import delta.music.Song;

public class AlbumPage extends WebPage
{
  private Long _key;
  private Album _album;
  private Interpret _interpret;
  private List<Song> _songs;

  @Override
  public void parseParameters() throws Exception
  {
    _key=ParameterFinder.getLongParameter(_request,"KEY",Long.valueOf(76));
  }

  @Override
  public void fetchData() throws Exception
  {
    ObjectsManager<Album> dsAlbum=MusicDataSource.getInstance().getAlbumDataSource();
    ObjectsManager<Song> dsSong=MusicDataSource.getInstance().getSongDataSource();
    _album=dsAlbum.load(_key);
    _interpret=_album.getInterpret();
    _songs=dsSong.loadRelation(Song.SONGS_RELATION,_key);
  }

  @Override
  public void generate(PrintWriter pw)
  {
    String title=_album.getTitle();
    WebPageTools.generatePageHeader(title,pw);
    pw.println("<HR WIDTH=\"100%\">");

    String image=_album.getImage();
    if(image!=null)
    {
      pw.println("<TABLE><TR><TD>");
      ImagePageParameters imagePage;
      imagePage=new ImagePageParameters(image);
      pw.print("<IMG SRC=\"");
      pw.print(imagePage.build());
      pw.println("\">");
      pw.println("</TD>");
      pw.println("<TD>");
    }
    pw.print("<H1>");
    pw.print(_album.getTitle());
    pw.println("</H1>");
    if(image!=null)
    {
      pw.println("</TD></TR></TABLE>");
    }

    // Songs
    pw.println("<OL>");
    int nbSongs=_songs.size();
    Song song=null;
    for(int i=0;i<nbSongs;i++)
    {
      song=_songs.get(i);
      pw.print("<LI><A HREF=\"");
      SongPageParameters songPage=new SongPageParameters(song.getPrimaryKey());
      pw.print(songPage.build());
      pw.print("\">");
      pw.print(song.getName());
      pw.println("</A>");
    }
    pw.println("</OL>");
    // End of page
    pw.println("<HR>");
    pw.print("<A HREF=\"");
    MusicMainPageParameters mmPage=new MusicMainPageParameters();
    pw.print(mmPage.build());
    pw.print("\">Paroles</A> :: ");
    if (_interpret!=null)
    {
      pw.print("<A HREF=\"");
      InterpretPageParameters interpretPage=new InterpretPageParameters(_interpret.getPrimaryKey());
      pw.print(interpretPage.build());
      pw.print("\">");
      pw.print(_interpret.getName());
      pw.print("</A> :: ");
    }
    pw.println(_album.getTitle());
    WebPageTools.generatePageFooter(pw);
  }
}
