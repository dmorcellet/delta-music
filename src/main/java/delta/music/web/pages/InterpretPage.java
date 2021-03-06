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

/**
 * Builder for the 'interpret' HTML page.
 * @author DAM
 */
public class InterpretPage extends WebPage
{
  private long _key;
  private Interpret _interpret;
  private List<Album> _albums;

  @Override
  public void parseParameters() throws Exception
  {
    _key=ParameterFinder.getLongParameter(_request,"KEY",Long.valueOf(76)).longValue();
  }

  @Override
  public void fetchData() throws Exception
  {
    ObjectsManager<Interpret> dsInterpret=MusicDataSource.getInstance().getInterpretDataSource();
    ObjectsManager<Album> dsAlbum=MusicDataSource.getInstance().getAlbumDataSource();
    _interpret=dsInterpret.load(Long.valueOf(_key));
    _albums=dsAlbum.loadRelation(Album.ALBUMS_RELATION,Long.valueOf(_key));
  }

  @Override
  public void generate(PrintWriter pw)
  {
    String title=_interpret.getName();
    WebPageTools.generatePageHeader(title,pw);
    pw.println("<HR WIDTH=\"100%\">");

    pw.print("<H1>");
    pw.print(_interpret.getName());
    pw.println("</H1>");

    // Albums
    pw.println("<UL>");
    int nbAlbums=_albums.size();
    Album album=null;
    for(int i=0;i<nbAlbums;i++)
    {
      album=_albums.get(i);
      pw.print("<LI><A HREF=\"");
      AlbumPageParameters albumPage=new AlbumPageParameters(album.getPrimaryKey().longValue());
      pw.print(albumPage.build());
      pw.print("\">");
      pw.print(album.getTitle());
      pw.print("</A>");
    }
    pw.println("</UL>");
    // End of page
    pw.println("<HR>");
    pw.print("<A HREF=\"");
    MusicMainPageParameters mmPage=new MusicMainPageParameters();
    pw.print(mmPage.build());
    pw.print("\">Paroles</A> :: ");
    pw.println(_interpret.getName());
    WebPageTools.generatePageFooter(pw);
  }
}
