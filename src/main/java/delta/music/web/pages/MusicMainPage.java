package delta.music.web.pages;

import java.io.PrintWriter;
import java.util.List;

import delta.common.framework.objects.data.ObjectsManager;
import delta.common.framework.web.WebPage;
import delta.common.framework.web.WebPageTools;
import delta.music.Interpret;
import delta.music.MusicDataSource;

/**
 * Main page for the 'music' site.
 * @author DAM
 */
public class MusicMainPage extends WebPage
{
  private List<Interpret> _interprets;

  @Override
  public void fetchData() throws Exception
  {
    ObjectsManager<Interpret> dsInterpret=MusicDataSource.getInstance().getInterpretDataSource();
    _interprets=dsInterpret.loadAll();
  }

  @Override
  public void generate(PrintWriter pw)
  {
    WebPageTools.generatePageHeader("Paroles",pw);
    pw.println("<HR WIDTH=\"100%\">");

    pw.print("<H1>");
    pw.print("Interprètes");
    pw.println("</H1>");

    // Interprets
    pw.println("<UL>");
    int nbInterpretes=_interprets.size();
    Interpret interpret=null;
    for(int i=0;i<nbInterpretes;i++)
    {
      interpret=_interprets.get(i);
      pw.print("<LI><A HREF=\"");
      InterpretPageParameters interpretPage=new InterpretPageParameters(interpret.getPrimaryKey().longValue());
      pw.print(interpretPage.build());
      pw.print("\">");
      pw.print(interpret.getName());
      pw.print("</A>");
    }
    pw.println("</UL>");
    // End of page
    pw.println("<HR>");
    pw.print("Paroles");
    WebPageTools.generatePageFooter(pw);
  }
}
