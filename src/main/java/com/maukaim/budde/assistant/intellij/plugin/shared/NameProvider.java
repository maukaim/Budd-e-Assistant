package com.maukaim.budde.assistant.intellij.plugin.shared;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class NameProvider {
    private static final String RESOURCE_FILE = "assets/default_assistant_name_list.txt";
    private static List<String> fakeNames = new ArrayList<>();

    public static String provideRandom() {
        if (fakeNames.isEmpty()) {
            loadNames();
        }

        return fakeNames.get(new Random().nextInt(fakeNames.size()-1));
    }

    private static void loadNames() {
        InputStream resourceAsStream = NameProvider.class.getClassLoader().getResourceAsStream(RESOURCE_FILE);
        List<String> allNames = new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines().collect(Collectors.toList());
        fakeNames.addAll(allNames);
    }
}
