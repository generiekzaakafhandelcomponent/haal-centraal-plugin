/*
 * Copyright 2015-2026 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ritense.valtimoplugins.haalcentraal.brp.personen.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.PersonenQuery
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.RaadpleegMetBurgerservicenummer
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.ZoekMetAdresseerbaarObjectIdentificatie
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.ZoekMetGeslachtsnaamEnGeboortedatum
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.ZoekMetNaamEnGemeenteVanInschrijving
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.ZoekMetNummeraanduidingIdentificatie
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.ZoekMetPostcodeEnHuisnummer
import com.ritense.valtimoplugins.haalcentraal.brp.personen.model.ZoekMetStraatHuisnummerEnGemeenteVanInschrijving
import com.ritense.valtimoplugins.haalcentraal.brp.personen.service.HaalCentraalBrpPersonenService
import com.ritense.valtimoplugins.haalcentraalauth.HaalCentraalAuthentication
import io.github.oshai.kotlinlogging.KotlinLogging
import org.operaton.bpm.engine.delegate.DelegateExecution
import java.net.URI

@Plugin(
    key = "haalcentraalbrppersonen",
    title = "Haal Centraal BRP Personen",
    description = "Haal Centraal BRP Personen plugin",
)
@Suppress("UNUSED")
class HaalCentraalBrpPersonenPlugin(
    private val haalCentraalBrpPersonenService: HaalCentraalBrpPersonenService,
) {
    @PluginProperty(key = "brpPersonenBaseUrl", secret = false, required = true)
    lateinit var brpPersonenBaseUrl: URI

    @PluginProperty(key = "authenticationPluginConfiguration", secret = false, required = true)
    lateinit var authenticationPluginConfiguration: HaalCentraalAuthentication

    @PluginAction(
        key = "hc-brp-personen-raadpleeg-bsn",
        title = "HC BRP Personen raadpleeg met burgerservicenummer",
        description = "Raadpleeg één of meer personen met burgerservicenummer",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START],
    )
    fun raadpleegMetBurgerservicenummer(
        @PluginActionProperty burgerservicenummer: String,
        @PluginActionProperty fields: String,
        @PluginActionProperty gemeenteVanInschrijving: String?,
        @PluginActionProperty resultProcessVariableName: String,
        execution: DelegateExecution,
    ) = getPersonen(
        execution = execution,
        resultProcessVariableName = resultProcessVariableName,
        query =
            RaadpleegMetBurgerservicenummer(
                fields = fields.toList(),
                burgerservicenummer = burgerservicenummer.toList(),
                gemeenteVanInschrijving = gemeenteVanInschrijving.nullIfBlank(),
            ),
    )

    @PluginAction(
        key = "hc-brp-personen-zoek-geslachtsnaam-geboortedatum",
        title = "HC BRP Personen zoek met geslachtsnaam en geboortedatum",
        description = "Zoek personen met geslachtsnaam en geboortedatum",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START],
    )
    fun zoekMetGeslachtsnaamEnGeboortedatum(
        @PluginActionProperty geslachtsnaam: String,
        @PluginActionProperty geboortedatum: String,
        @PluginActionProperty geslacht: String?,
        @PluginActionProperty voorvoegsel: String?,
        @PluginActionProperty voornamen: String?,
        @PluginActionProperty inclusiefOverledenPersonen: Boolean?,
        @PluginActionProperty gemeenteVanInschrijving: String?,
        @PluginActionProperty fields: String,
        @PluginActionProperty resultProcessVariableName: String,
        execution: DelegateExecution,
    ) = getPersonen(
        execution = execution,
        resultProcessVariableName = resultProcessVariableName,
        query =
            ZoekMetGeslachtsnaamEnGeboortedatum(
                fields = fields.toList(),
                geslachtsnaam = geslachtsnaam,
                geboortedatum = geboortedatum,
                geslacht = geslacht.nullIfBlank(),
                voorvoegsel = voorvoegsel.nullIfBlank(),
                voornamen = voornamen.nullIfBlank(),
                inclusiefOverledenPersonen = inclusiefOverledenPersonen,
                gemeenteVanInschrijving = gemeenteVanInschrijving.nullIfBlank(),
            ),
    )

    @PluginAction(
        key = "hc-brp-personen-zoek-naam-gemeente",
        title = "HC BRP Personen zoek met naam en gemeente van inschrijving",
        description = "Zoek personen met geslachtsnaam, voornamen en gemeente van inschrijving",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START],
    )
    fun zoekMetNaamEnGemeenteVanInschrijving(
        @PluginActionProperty geslachtsnaam: String,
        @PluginActionProperty voornamen: String,
        @PluginActionProperty gemeenteVanInschrijving: String,
        @PluginActionProperty geslacht: String?,
        @PluginActionProperty voorvoegsel: String?,
        @PluginActionProperty inclusiefOverledenPersonen: Boolean?,
        @PluginActionProperty fields: String,
        @PluginActionProperty resultProcessVariableName: String,
        execution: DelegateExecution,
    ) = getPersonen(
        execution = execution,
        resultProcessVariableName = resultProcessVariableName,
        query =
            ZoekMetNaamEnGemeenteVanInschrijving(
                fields = fields.toList(),
                geslachtsnaam = geslachtsnaam,
                voornamen = voornamen,
                gemeenteVanInschrijving = gemeenteVanInschrijving,
                geslacht = geslacht.nullIfBlank(),
                voorvoegsel = voorvoegsel.nullIfBlank(),
                inclusiefOverledenPersonen = inclusiefOverledenPersonen,
            ),
    )

    @PluginAction(
        key = "hc-brp-personen-zoek-postcode-huisnummer",
        title = "HC BRP Personen zoek met postcode en huisnummer",
        description = "Zoek personen met postcode en huisnummer",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START],
    )
    fun zoekMetPostcodeEnHuisnummer(
        @PluginActionProperty postcode: String,
        @PluginActionProperty huisnummer: Int,
        @PluginActionProperty huisletter: String?,
        @PluginActionProperty huisnummertoevoeging: String?,
        @PluginActionProperty geboortedatum: String?,
        @PluginActionProperty geslachtsnaam: String?,
        @PluginActionProperty inclusiefOverledenPersonen: Boolean?,
        @PluginActionProperty gemeenteVanInschrijving: String?,
        @PluginActionProperty fields: String,
        @PluginActionProperty resultProcessVariableName: String,
        execution: DelegateExecution,
    ) = getPersonen(
        execution = execution,
        resultProcessVariableName = resultProcessVariableName,
        query =
            ZoekMetPostcodeEnHuisnummer(
                fields = fields.toList(),
                postcode = postcode,
                huisnummer = huisnummer,
                huisletter = huisletter.nullIfBlank(),
                huisnummertoevoeging = huisnummertoevoeging.nullIfBlank(),
                geboortedatum = geboortedatum.nullIfBlank(),
                geslachtsnaam = geslachtsnaam.nullIfBlank(),
                inclusiefOverledenPersonen = inclusiefOverledenPersonen,
                gemeenteVanInschrijving = gemeenteVanInschrijving.nullIfBlank(),
            ),
    )

    @PluginAction(
        key = "hc-brp-personen-zoek-straat-huisnummer-gemeente",
        title = "HC BRP Personen zoek met straat, huisnummer en gemeente van inschrijving",
        description = "Zoek personen met straat, huisnummer en gemeente van inschrijving",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START],
    )
    fun zoekMetStraatHuisnummerEnGemeenteVanInschrijving(
        @PluginActionProperty straat: String,
        @PluginActionProperty huisnummer: Int,
        @PluginActionProperty gemeenteVanInschrijving: String,
        @PluginActionProperty huisletter: String?,
        @PluginActionProperty huisnummertoevoeging: String?,
        @PluginActionProperty inclusiefOverledenPersonen: Boolean?,
        @PluginActionProperty fields: String,
        @PluginActionProperty resultProcessVariableName: String,
        execution: DelegateExecution,
    ) = getPersonen(
        execution = execution,
        resultProcessVariableName = resultProcessVariableName,
        query =
            ZoekMetStraatHuisnummerEnGemeenteVanInschrijving(
                fields = fields.toList(),
                straat = straat,
                huisnummer = huisnummer,
                gemeenteVanInschrijving = gemeenteVanInschrijving,
                huisletter = huisletter.nullIfBlank(),
                huisnummertoevoeging = huisnummertoevoeging.nullIfBlank(),
                inclusiefOverledenPersonen = inclusiefOverledenPersonen,
            ),
    )

    @PluginAction(
        key = "hc-brp-personen-zoek-nummeraanduiding",
        title = "HC BRP Personen zoek met nummeraanduiding identificatie",
        description = "Zoek personen met nummeraanduiding identificatie",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START],
    )
    fun zoekMetNummeraanduidingIdentificatie(
        @PluginActionProperty nummeraanduidingIdentificatie: String,
        @PluginActionProperty inclusiefOverledenPersonen: Boolean?,
        @PluginActionProperty gemeenteVanInschrijving: String?,
        @PluginActionProperty fields: String,
        @PluginActionProperty resultProcessVariableName: String,
        execution: DelegateExecution,
    ) = getPersonen(
        execution = execution,
        resultProcessVariableName = resultProcessVariableName,
        query =
            ZoekMetNummeraanduidingIdentificatie(
                fields = fields.toList(),
                nummeraanduidingIdentificatie = nummeraanduidingIdentificatie,
                inclusiefOverledenPersonen = inclusiefOverledenPersonen,
                gemeenteVanInschrijving = gemeenteVanInschrijving.nullIfBlank(),
            ),
    )

    @PluginAction(
        key = "hc-brp-personen-zoek-adresseerbaar-object",
        title = "HC BRP Personen zoek met adresseerbaar object identificatie",
        description = "Zoek personen met adresseerbaar object identificatie",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START],
    )
    fun zoekMetAdresseerbaarObjectIdentificatie(
        @PluginActionProperty adresseerbaarObjectIdentificatie: String,
        @PluginActionProperty inclusiefOverledenPersonen: Boolean?,
        @PluginActionProperty gemeenteVanInschrijving: String?,
        @PluginActionProperty fields: String,
        @PluginActionProperty resultProcessVariableName: String,
        execution: DelegateExecution,
    ) = getPersonen(
        execution = execution,
        resultProcessVariableName = resultProcessVariableName,
        query =
            ZoekMetAdresseerbaarObjectIdentificatie(
                fields = fields.toList(),
                adresseerbaarObjectIdentificatie = adresseerbaarObjectIdentificatie,
                inclusiefOverledenPersonen = inclusiefOverledenPersonen,
                gemeenteVanInschrijving = gemeenteVanInschrijving.nullIfBlank(),
            ),
    )

    private fun getPersonen(
        execution: DelegateExecution,
        resultProcessVariableName: String,
        query: PersonenQuery,
    ) {
        logger.info { "Retrieving personen (${query.type}) for case ${execution.businessKey}" }

        val personen =
            haalCentraalBrpPersonenService.getPersonen(
                baseUrl = brpPersonenBaseUrl,
                query = query,
                authentication = authenticationPluginConfiguration,
            )

        execution.processInstance.setVariable(resultProcessVariableName, personen)
    }

    private fun String.toList(): List<String> =
        split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }

    private fun String?.nullIfBlank(): String? = this?.takeIf { it.isNotBlank() }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
