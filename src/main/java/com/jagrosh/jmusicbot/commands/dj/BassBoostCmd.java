package com.jagrosh.jmusicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

public class BassBoostCmd extends DJCommand
{
    private static final float[] BASS_BOOST = { 0.3f, 0.25f, 0.2f, 0.15f, 0.1f, 0.05f, 0.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 0.0f, 0.0f };

    private static final float MULT_CONSTANT = 0.01f;

    private final EqualizerFactory equalizer;

    public BassBoostCmd(Bot bot)
    {
        super(bot);
        this.name = "bassboost";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.equalizer = new EqualizerFactory();
        this.help = "sets or shows bass boost (default 0)";
        this.arguments = "[0-100]";
        for (int i = 0; i < BASS_BOOST.length; i++) {
            equalizer.setGain(i, BASS_BOOST[i]);
        }
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        AudioPlayer player = handler.getPlayer();
        int bassboost = Math.round(this.equalizer.getGain(BASS_BOOST.length - 1) / MULT_CONSTANT);
        player.setFilterFactory(equalizer);
        if(event.getArgs().isEmpty())
        {
            event.reply(FormatUtil.volumeIcon(bassboost)+" Current bass boost level is `"+bassboost+"`");
        }
        else
        {
            int nbassboost;
            try{
                nbassboost = Integer.parseInt(event.getArgs());
            }catch(NumberFormatException e){
                nbassboost = -1;
            }
            if(nbassboost<0 || nbassboost>100)
                event.reply(event.getClient().getError()+
                        " Bass boost level must be a valid integer between 0 and 100!");
            else
            {
                for (int i = 0; i < BASS_BOOST.length; i++) {
                    equalizer.setGain(i, BASS_BOOST[i] + MULT_CONSTANT * nbassboost);
                }
                event.reply(FormatUtil.volumeIcon(nbassboost)+
                        " Bass boost changed to `"+nbassboost+"`");
            }
        }
    }

}