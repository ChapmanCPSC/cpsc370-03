package com.applica.applicaandroid;

import android.content.Context;
import android.graphics.Typeface;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class ApplicaFont implements ITypeface {
    private static final String TTF_FILE = "applica_font.ttf";

    private static Typeface typeface = null;

    private static HashMap<String, Character> mChars;

    @Override
    public IIcon getIcon(String key) {
        return Icon.valueOf(key);
    }

    @Override
    public HashMap<String, Character> getCharacters() {
        if (mChars == null) {
            HashMap<String, Character> aChars = new HashMap<String, Character>();
            for (Icon v : Icon.values()) {
                aChars.put(v.name(), v.character);
            }
            mChars = aChars;
        }

        return mChars;
    }

    @Override
    public String getMappingPrefix() {
        return "apf";
    }

    @Override
    public String getFontName() {
        return "ApplicaFont";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public int getIconCount() {
        return mChars.size();
    }

    @Override
    public Collection<String> getIcons() {
        Collection<String> icons = new LinkedList<String>();

        for (Icon value : Icon.values()) {
            icons.add(value.name());
        }

        return icons;
    }


    @Override
    public String getAuthor() {
        return "Jill Tiutan";
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getLicense() {
        return "";
    }

    @Override
    public String getLicenseUrl() {
        return "";
    }

    @Override
    public Typeface getTypeface(Context context) {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + TTF_FILE);
            } catch (Exception e) {
                return null;
            }
        }
        return typeface;
    }

    public static enum Icon implements IIcon {
        applica_logo('\ue600'),
        email_icon('\ue601'),
        password_icon('\ue602'),
        lock_icon('\ue602'),
        new_password_icon('\ue603'),
        unlocked_icon('\ue603'),
        confirm_password_icon('\ue604'),
        check_icon('\ue604'),
        newsfeed_icon('\ue605'),
        profile_icon('\ue607'),
        school_list_icon('\ue606'),
        cart_icon('\ue608'),
        step_1_icon('\ue67d'),
        step_2_icon('\ue67f'),
        step_3_icon('\ue681'),
        step_4_icon('\ue683'),
        forward_arrow('\ue634'),
        plus_icon('\ue64f'),
        x_icon('\ue684'),
        search_bar_icon('\ue678'),
        search_icon('\ue67a'),
        settings_icon_dots('\ue67b'),
        side_menu_icon('\ue67c'),
        drop_down_arrow('\ue627');

        char character;

        Icon(char character) {
            this.character = character;
        }

        public String getFormattedName() {
            return "{" + name() + "}";
        }

        public char getCharacter() {
            return character;
        }

        public String getName() {
            return name();
        }

        // remember the typeface so we can use it later
        private static ITypeface typeface;

        public ITypeface getTypeface() {
            if (typeface == null) {
                typeface = new ApplicaFont();
            }
            return typeface;
        }
    }
}