package delta.music.web.pages;

import java.io.PrintWriter;

import delta.common.framework.objects.data.ObjectsManager;
import delta.common.framework.web.PageParameters;
import delta.common.framework.web.WebPage;
import delta.common.framework.web.WebPageTools;
import delta.common.utils.ParameterFinder;
import delta.common.utils.text.EndOfLine;
import delta.music.Album;
import delta.music.Interpret;
import delta.music.MusicDataSource;
import delta.music.Song;

/**
 * Builder for the 'song' HTML page.
 * @author DAM
 */
public class SongPage extends WebPage
{
  private static final String SEPARATOR=" :: ";
  private long _key;
  private Album _album;
  private Interpret _interpret;
  private Song _song;

  @Override
  public void parseParameters() throws Exception
  {
    _key=ParameterFinder.getLongParameter(_request,"KEY",Long.valueOf(76)).longValue();
  }

  @Override
  public void fetchData() throws Exception
  {
    ObjectsManager<Song> dsSong=MusicDataSource.getInstance().getSongDataSource();
    _song=dsSong.load(Long.valueOf(_key));
    _album=_song.getAlbum();
    _interpret=_album.getInterpret();
  }

  @Override
  public void generate(PrintWriter pw)
  {
    String title=_song.getName();
    WebPageTools.generatePageHeader(title,pw);
    pw.print("<H1>");
    pw.print(title);
    pw.println("</H1>");
    String text=_song.getText();
    text=text.replaceAll(EndOfLine.WINDOWS.getValue(), "<BR>"+EndOfLine.NATIVE_EOL);
    text=text.replaceAll(EndOfLine.UNIX.getValue(), "<BR>"+EndOfLine.NATIVE_EOL);
    pw.print(text);
    pw.println("<HR>");
    MusicMainPageParameters mmPage=new MusicMainPageParameters();
    printLink(pw,mmPage,"Paroles");
    pw.print(SEPARATOR);
    if (_interpret!=null)
    {
      InterpretPageParameters interpretPage=new InterpretPageParameters(_interpret.getPrimaryKey().longValue());
      printLink(pw,interpretPage,_interpret.getName());
      pw.print(SEPARATOR);
    }
    if (_album!=null)
    {
      AlbumPageParameters albumPage=new AlbumPageParameters(_album.getPrimaryKey().longValue());
      printLink(pw,albumPage,_album.getTitle());
      pw.print(SEPARATOR);
    }
    pw.println(_song.getName());
    WebPageTools.generatePageFooter(pw);
  }

  private void printLink(PrintWriter pw, PageParameters parameters, String label)
  {
    pw.print("<A HREF=\"");
    pw.print(parameters.build());
    pw.print("\">");
    pw.print(label);
    pw.print("</A>");
  }
}
