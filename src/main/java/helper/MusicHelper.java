package helper;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import modcore.MizukiModCore;


public class MusicHelper
{
    private String introString;
    private String loopString;
    private TempMusic intro;
    private TempMusic loop;
    private boolean next;
    private boolean introEnd;
    private boolean isPlaying;
    private float introTime;

    public MusicHelper()
    {
        this.isPlaying = false;
        this.introEnd = false;
    }

    public void updateVolume()
    {
        if (intro != null && !intro.isDone)
        {
            intro.updateVolume();
        }

        if (loop != null && !loop.isDone)
        {
            loop.updateVolume();
        }
    }

    public void update()
    {
        if (intro != null && !intro.isDone)
        {
            intro.update();
            if (intro.isDone)
            {
                intro = null;
            }
        }

        if (loop != null && !loop.isDone)
        {
            loop.update();
            if (loop.isDone)
            {
                loop = null;
            }
        }

        if (isPlaying)
        {
            if (intro != null)
            {
                if (this.introTime < 0.2 && !next)
                {
                    this.next = true;
                    MizukiModCore.logger.info("music:" + this.introString);
                    this.loop = new TempMusic(this.loopString, true, true);
                }

                if (this.introTime > 0)
                {
                    this.introTime -= Gdx.graphics.getDeltaTime();
                }
                else
                {
                    intro.isDone = true;
                }

                if (intro.isDone && !introEnd)
                {
                    introEnd = true;
                    intro.kill();
                    intro = null;
                }
            }
        }
    }

    public void playBGM(String introString, String loopString)
    {
        this.isPlaying = true;
        this.introEnd = false;
        this.next = false;
        this.introString = introString;
        this.loopString = loopString;

        CardCrawlGame.music.silenceTempBgmInstantly();
        this.intro = new TempMusic(introString, true, false);


        if (introString.equals("MIZUKI_BOSS1_INTRO"))
        {
            this.introTime = 15f;
        }
        if (introString.equals("MIZUKI_BOSS2_1_INTRO"))
        {
            this.introTime = 18.002f;
        }
        if (introString.equals("MIZUKI_BOSS2_2_INTRO"))
        {
            this.introTime = 53.322f;
        }
        if (introString.equals("MIZUKI_BOSS3_1_INTRO"))
        {
            this.introTime = 13.714f;
        }
        if (introString.equals("MIZUKI_BOSS3_2_INTRO"))
        {
            this.introTime = 38.4f;
        }
        if (introString.equals("MIZUKI_BOSS4_INTRO"))
        {
            this.introTime = 26.4f;
        }
    }

    public void stopLoop()
    {
        MizukiModCore.logger.info("StopLoop");
        this.isPlaying = false;
        this.introEnd = false;

        if (intro != null && !intro.isDone)
        {
            this.intro.fadeOut();
        }
        if (loop != null && !loop.isDone)
        {
            this.loop.fadeOut();
        }
    }

    public void fadeOut()
    {
        MizukiModCore.logger.info("FadeOut");
        if (intro != null && !intro.isDone)
        {
            this.intro.fadeOut();
        }
        if (loop != null && !loop.isDone)
        {
            this.loop.fadeOut();
        }
    }

    public void silence()
    {
        MizukiModCore.logger.info("Silence");
        if (intro != null && !intro.isDone)
        {
            this.intro.silenceInstantly();
            this.intro.isFadingOut = true;
        }
        if (loop != null && !loop.isDone)
        {
            this.loop.silenceInstantly();
            this.loop.isFadingOut = true;
        }
    }

    public void kill()
    {
        MizukiModCore.logger.info("Kill");
        if (intro != null && !intro.isDone)
        {
            this.intro.kill();
        }
        if (loop != null && !loop.isDone)
        {
            this.loop.kill();
        }
    }
}
