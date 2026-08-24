package org.forbidec.service.mention;

/** Le résultat de la résolution d'une note vers sa mention (barème). */
public record Mention(String longue, String courte) {
    static final Mention AUCUNE = new Mention(null, null);
}
