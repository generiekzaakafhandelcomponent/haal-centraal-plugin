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

import {PluginConfigurationData} from '@valtimo/plugin';

interface BrpPersonenConfig extends PluginConfigurationData {
    brpPersonenBaseUrl: string;
    authenticationPluginConfiguration: string;
}

interface BrpPersonenBaseActionConfig {
    fields: string;
    resultProcessVariableName: string;
    inclusiefOverledenPersonen?: boolean;
    gemeenteVanInschrijving?: string;
}

interface RaadpleegMetBsnConfig extends BrpPersonenBaseActionConfig {
    burgerservicenummer: string;
}

interface ZoekMetGeslachtsnaamEnGeboortedatumConfig extends BrpPersonenBaseActionConfig {
    geslachtsnaam: string;
    geboortedatum: string;
    geslacht?: string;
    voorvoegsel?: string;
    voornamen?: string;
}

interface ZoekMetNaamEnGemeenteConfig extends BrpPersonenBaseActionConfig {
    geslachtsnaam: string;
    voornamen: string;
    gemeenteVanInschrijving: string;
    geslacht?: string;
    voorvoegsel?: string;
}

interface ZoekMetPostcodeEnHuisnummerConfig extends BrpPersonenBaseActionConfig {
    postcode: string;
    huisnummer: string;
    huisletter?: string;
    huisnummertoevoeging?: string;
    geboortedatum?: string;
    geslachtsnaam?: string;
}

interface ZoekMetStraatHuisnummerEnGemeenteConfig extends BrpPersonenBaseActionConfig {
    straat: string;
    huisnummer: string;
    gemeenteVanInschrijving: string;
    huisletter?: string;
    huisnummertoevoeging?: string;
}

interface ZoekMetNummeraanduidingConfig extends BrpPersonenBaseActionConfig {
    nummeraanduidingIdentificatie: string;
}

interface ZoekMetAdresseerbaarObjectConfig extends BrpPersonenBaseActionConfig {
    adresseerbaarObjectIdentificatie: string;
}

export {
    BrpPersonenConfig,
    BrpPersonenBaseActionConfig,
    RaadpleegMetBsnConfig,
    ZoekMetGeslachtsnaamEnGeboortedatumConfig,
    ZoekMetNaamEnGemeenteConfig,
    ZoekMetPostcodeEnHuisnummerConfig,
    ZoekMetStraatHuisnummerEnGemeenteConfig,
    ZoekMetNummeraanduidingConfig,
    ZoekMetAdresseerbaarObjectConfig,
};
