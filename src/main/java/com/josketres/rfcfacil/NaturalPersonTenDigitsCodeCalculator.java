package com.josketres.rfcfacil;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculates the first ten digits of the RFC for a natural person.
 */
class NaturalPersonTenDigitsCodeCalculator {

    private static final Pattern VOWEL_PATTERN = Pattern.compile("[AEIOU]+");

    private final NaturalPerson person;

    private static final String[] SPECIAL_PARTICLES =
            {"DAS", "DA", "DEL", "DER", "DE", "DIE", "DI", "DD", "EL", "LES", "LA", "LOS", "LAS", "LES", "LE", "MAC", "MC", "VAN", "VON", "Y"};

    private static final String[] FORBIDDEN_WORDS = {"BACA", "BAKA", "BUEI", "BUEY", "CACA", "CACO", "CAGA", "CAGO", "CAKA", "CAKO", "COGE", "COGI", "COJA", "COJE", "COJI", "COJO", "COLA", "CULO", "FALO", "FETO", "GETA", "GUEI", "GUEY", "JETA", "JOTO", "KACA", "KACO", "KAGA", "KAGO", "KAKA", "KAKO", "KOGE", "KOGI", "KOJA", "KOJE", "KOJI", "KOJO", "KOLA", "KULO", "LILO", "LOCA", "LOCO", "LOKA", "LOKO", "MAME", "MAMO", "MEAR", "MEAS", "MEON", "MIAR", "MION", "MOCO", "MOKO", "MULA", "MULO", "NACA", "NACO", "PEDA", "PEDO", "PENE", "PIPI", "PITO", "POPO", "PUTA", "PUTO", "QULO", "RATA", "ROBA", "ROBE", "ROBO", "RUIN", "SENO", "TETA", "VACA", "VAGA", "VAGO", "VAKA", "VUEI", "VUEY", "WUEI", "WUEY"};

    NaturalPersonTenDigitsCodeCalculator(NaturalPerson person) {
        this.person = person;
    }

    public String calculate() {
        String a = nameCode();
        String b = StringUtils.stripAccents(a);
        return obfuscateForbiddenWords(b) + birthdayCode();
    }

    private String obfuscateForbiddenWords(String nameCode) {
        for (String forbidden : FORBIDDEN_WORDS) {
            if (forbidden.equals(nameCode)) {
                return nameCode.substring(0, 3) + "X";
            }
        }
        return nameCode;
    }

    private String nameCode() {
        if (isFirstLastNameEmpty()) {
            return firstLastNameEmptyForm();
        } else if (isSecondLastNameEmpty()) {
            return secondLastNameEmptyForm();
        } else if (isFirstLastNameIsTooShort()) {
            return firstLastNameTooShortForm();
        } else {
            return normalForm();
        }
    }

    private String secondLastNameEmptyForm() {
        return firstTwoLettersOf(person.firstLastName)
                + firstTwoLettersOf(filterName(person.name));
    }

    private String birthdayCode() {
        return lastTwoDigitsOf(person.year)
                + formattedInTwoDigits(person.month)
                + formattedInTwoDigits(person.day);
    }

    private boolean isSecondLastNameEmpty() {
        return StringUtils.isEmpty(normalize(person.secondLastName));
    }

    private String firstLastNameEmptyForm() {
        return firstTwoLettersOf(person.secondLastName)
                + firstTwoLettersOf(filterName(person.name));
    }

    private boolean isFirstLastNameEmpty() {
        return StringUtils.isEmpty(normalize(person.firstLastName));
    }

    private String firstLastNameTooShortForm() {
        return firstLetterOf(person.firstLastName)
                + firstLetterOf(person.secondLastName)
                + firstTwoLettersOf(filterName(person.name));
    }

    private String firstTwoLettersOf(String word) {
        String normalizedWord = normalize(word).replace(" ", "");
        return normalizedWord.length() > 1 ? normalizedWord.substring(0, 2) : normalizedWord.concat("X");
    }

    private boolean isFirstLastNameIsTooShort() {
        return normalize(person.firstLastName).length() <= 2;
    }

    private String normalForm() {
        return firstLetterOf(person.firstLastName)
                + firstVowelExcludingFirstCharacterOf(person.firstLastName)
                + firstLetterOf(person.secondLastName)
                + firstLetterOf(filterName(person.name));
    }

    private String filterName(String name) {
        return normalize(name).trim()
                .replaceFirst("^(MARIA|MA\\.|MA|M\\.|M|JOSE|J|J\\.|DA|DAS|DE|DEL|DER|DI|DIE|DD|EL|LA|LAS|LOS|LE|LES|MAC|MC|VAN|VON|Y)\\s+", "");
    }

    private String formattedInTwoDigits(int number) {
        return String.format(Locale.getDefault(), "%02d", number);
    }

    private String lastTwoDigitsOf(int number) {
        return formattedInTwoDigits(number % 100);
    }

    private String firstLetterOf(String word) {
        String normalizedWord = normalize(word);
        return String.valueOf(normalizedWord.charAt(0));
    }

    private String normalize(String word) {
        if (StringUtils.isEmpty(word))
            return word;

        String cleanedWord = word.replaceAll("[\\-.',´`’\\\\/]", "");
        if (StringUtils.isEmpty(cleanedWord)) {
            return cleanedWord;
        }

        String normalizedWord = stripAccentsExcludingNTilde(cleanedWord);

        return removeSpecialParticles(normalizedWord);
    }

    private String removeSpecialParticles(String word) {
        String particlesRegex = String.join("|", SPECIAL_PARTICLES);  // convert the array to a regex OR sequence
        Pattern pattern = Pattern.compile("\\b(" + particlesRegex + ")\\b", Pattern.CASE_INSENSITIVE); // match the particles only if they are whole words
        Matcher matcher = pattern.matcher(word);
        String result = matcher.replaceAll(" "); // replace all special particles with space
        return result.trim();  // remove any leading and trailing spaces
    }

    private String firstVowelExcludingFirstCharacterOf(String word) {
        String normalizedWord = normalize(word).substring(1);
        Matcher m = VOWEL_PATTERN.matcher(normalizedWord);
        if (!m.find()) {
            return "X";
        }
        return String.valueOf(normalizedWord.charAt(m.start()));
    }

    private String stripAccentsExcludingNTilde(String input) {
        if (StringUtils.isEmpty(input))
            return input;

        input = input.toUpperCase();
        input = input.replace("Ñ", "$");
        input = StringUtils.stripAccents(input);
        input = input.replace("$", "Ñ");
        return input.replaceAll("[^A-Z0-9&Ñ ]", "");
    }
}
