package delta.music.web.pages;

import java.io.PrintWriter;

import delta.common.framework.objects.data.ObjectSource;
import delta.common.framework.web.WebPage;
import delta.common.framework.web.WebPageTools;
import delta.common.utils.ParameterFinder;
import delta.common.utils.text.EndOfLine;
import delta.music.Album;
import delta.music.Interpret;
import delta.music.MusicDataSource;
import delta.music.Song;

public class SongPage extends WebPage
{
  private long _key;
  private Album _album;
  private Interpret _interpret;
  private Song _song;

  @Override
  public void parseParameters() throws Exception
  {
    _key=ParameterFinder.getLongParameter(_request,"KEY",76);
  }

  @Override
  public void fetchData() throws Exception
  {
    ObjectSource<Song> dsSong=MusicDataSource.getInstance().getSongDataSource();
    _song=dsSong.load(_key);
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
    text=text.replaceAll(EndOfLine.NATIVE_EOL, "<BR>"+EndOfLine.NATIVE_EOL);
    pw.print(text);
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
    pw.print("<A HREF=\"");
    if (_album!=null)
    {
      AlbumPageParameters albumPage=new AlbumPageParameters(_album.getPrimaryKey());
      pw.print(albumPage.build());
      pw.print("\">");
      pw.print(_album.getTitle());
      pw.print("</A> :: ");
    }
    pw.println(_song.getName());
    WebPageTools.generatePageFooter(pw);
  }
}
