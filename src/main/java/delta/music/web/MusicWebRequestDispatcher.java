package delta.music.web;

import delta.common.framework.web.WebRequestDispatcher;
import delta.music.web.pages.AlbumPage;
import delta.music.web.pages.AlbumPageParameters;
import delta.music.web.pages.ImagePage;
import delta.music.web.pages.ImagePageParameters;
import delta.music.web.pages.InterpretPage;
import delta.music.web.pages.InterpretPageParameters;
import delta.music.web.pages.MusicMainPage;
import delta.music.web.pages.MusicMainPageParameters;
import delta.music.web.pages.SongPage;
import delta.music.web.pages.SongPageParameters;

public class MusicWebRequestDispatcher extends WebRequestDispatcher
{
  public MusicWebRequestDispatcher()
  {
    super();
    addNewActionPage(MusicMainPageParameters.ACTION_VALUE,MusicMainPage.class);
    addNewActionPage(InterpretPageParameters.ACTION_VALUE,InterpretPage.class);
    addNewActionPage(AlbumPageParameters.ACTION_VALUE,AlbumPage.class);
    addNewActionPage(SongPageParameters.ACTION_VALUE,SongPage.class);
    addNewActionPage(ImagePageParameters.ACTION_VALUE,ImagePage.class);
  }
}
