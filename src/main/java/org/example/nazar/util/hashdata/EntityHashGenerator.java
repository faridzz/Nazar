package org.example.nazar.util.hashdata;

import java.time.LocalDate;

import static org.example.nazar.util.hashdata.HashUtils.generateHash;

/**
 * Utility class for generating hashes for entities.
 */
public class EntityHashGenerator {


    private EntityHashGenerator() {

    }

    /**
     * Generates a hash string for the given entity data.
     *
     * @param author   the author of the content
     * @param content  the content to hash
     * @param postedAt the date when the content was posted
     * @return the generated hash string
     */
    public static String reviewHash(String author, String content, LocalDate postedAt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(author)
                .append(content)
                .append(postedAt.toString());
        return generateHash(stringBuilder.toString(), "MD5");
    }
}
