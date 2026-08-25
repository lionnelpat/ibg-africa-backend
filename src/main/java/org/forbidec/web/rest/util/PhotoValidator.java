package org.forbidec.web.rest.util;

import org.forbidec.web.rest.errors.BadRequestAlertException;

/**
 * Garde-fou serveur pour les champs photo (BLOB) exposés par les DTO
 * Etudiant/Enseignant : la limite de taille et le type de contenu ne
 * sont vérifiés que côté client (Angular) sinon, ce qui est
 * contournable par n'importe quel appel direct à l'API.
 */
public final class PhotoValidator {

    private static final int MAX_PHOTO_BYTES = 3 * 1024 * 1024; // 3 Mo, aligné sur la limite frontend

    private PhotoValidator() {}

    public static void verifier(byte[] photo, String photoContentType, String entityName) {
        if (photo == null) {
            return;
        }
        if (photo.length > MAX_PHOTO_BYTES) {
            throw new BadRequestAlertException("La photo dépasse la taille maximale autorisée (3 Mo).", entityName, "photosize");
        }
        if (photoContentType == null || !photoContentType.startsWith("image/")) {
            throw new BadRequestAlertException("Le type de fichier de la photo doit être une image.", entityName, "phototype");
        }
    }
}
