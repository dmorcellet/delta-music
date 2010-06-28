package delta.music.cddb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import delta.common.utils.NumericTools;
import delta.common.utils.files.TextFileReader;
import delta.common.utils.files.iterator.AbstractFileIteratorCallback;
import delta.common.utils.files.iterator.FileIterator;
import delta.common.utils.files.iterator.FileIteratorCallback;

public class CDDBFileReader
{
  public static final String TRACK_FRAME_OFFSETS="# Track frame offsets:";
  public static final String DISCID="DISCID=";
  public static final String DTITLE="DTITLE=";
  public static final String DYEAR="DYEAR=";
  public static final String TTITLE="TTITLE";
  public static final String PLAYORDER="PLAYORDER";

  private File _f;
  public CDDBFileReader(File f)
  {
    _f=f;
  }

  public void read()
  {
    TextFileReader fp=new TextFileReader(_f);
    if (fp.start())
    {
      String line=null;
      String tmp;
      String discId="",discTitle="";
      while(true)
      {
        line=fp.getNextLine();
        if (line==null) break;
        if (line.startsWith(TRACK_FRAME_OFFSETS))
        {
          // Track offsets
          List<Integer> trackOffsets=new ArrayList<Integer>();
          while (true)
          {
            line=fp.getNextLine();
            if (line==null) break;
            if (line.length()==0) break;
            if (line.charAt(0)=='#')
            {
              int firstNonBlanck=1;
              int longueur=line.length();
              while ((firstNonBlanck<longueur) && ((line.charAt(firstNonBlanck)==' ') || (line.charAt(firstNonBlanck)=='\t')))
                firstNonBlanck++;
              if (firstNonBlanck<longueur)
              {
                tmp=line.substring(firstNonBlanck).trim();
                int offset=NumericTools.parseInt(tmp,-1);
                if (offset!=-1)
                  trackOffsets.add(Integer.valueOf(offset));
                else break;
              }
            }
          }

          // Disk ID & title & track titles
          List<String> trackTitles=new ArrayList<String>(trackOffsets.size());
          while (true)
          {
            line=fp.getNextLine();
            if (line==null) break;
            if (line.startsWith("#")) continue;
            if (line.startsWith(PLAYORDER)) break;
            if (line.startsWith(DISCID)) discId=line.substring(DISCID.length()).trim();
            if (line.startsWith(DTITLE)) discTitle=line.substring(DTITLE.length()).trim();
            if (line.startsWith(TTITLE))
            {
              int indexOfEgal=line.indexOf('=');
              if (indexOfEgal!=-1)
                trackTitles.add(line.substring(indexOfEgal+1).trim());
            }
          }
          if ((discTitle.indexOf("Pink Floyd")!=-1)
              && (discTitle.indexOf("Ummagumma")!=-1))
          {
            System.out.println("ID="+discId);
            System.out.println("Disc title="+discTitle);
            System.out.println("NB tracks = "+trackOffsets.size());
            System.out.println("Tracks titles = "+trackTitles);
          }
        }
      }
      fp.terminate();
    }
    //System.out.println("Nb albums dans fichiers : "+nb);
  }

  public static void main(String[] args)
  {
    File f=new File("C:\\dada\\atrier\\cddb\\freedb-win-20050605\\rock");
    FileIteratorCallback fic=new AbstractFileIteratorCallback()
    {
      @Override
      public void handleFile(File absolute, File relative)
      {
        //System.out.println("Fichier : "+f.getAbsolutePath());
        new CDDBFileReader(absolute).read();
      }
    };
    FileIterator fi=new FileIterator(f,true,fic);
    fi.run();
  }
}
