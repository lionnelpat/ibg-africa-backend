/**
 * Domain objects.
 *
 * <p>{@code paysFilter} is the row-level security filter behind le
 * cloisonnement par pays : {@link org.forbidec.security.PaysFilterInterceptor}
 * enables it on the Hibernate session for every authenticated request, with
 * the pays id(s) the current user is allowed to see. Entities scoped by pays
 * declare their own {@code @Filter(name = "paysFilter", condition = ...)}.
 */
@org.hibernate.annotations.FilterDef(
    name = "paysFilter",
    parameters = @org.hibernate.annotations.ParamDef(name = "paysIds", type = Long.class)
)
package org.forbidec.domain;
