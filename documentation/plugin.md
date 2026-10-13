# Haal Centraal Plugin

## BRP Personen

Plugin key: `haalcentraalbrppersonen`. Implements the [BRP API Personen](https://developer.rvig.nl/brp-api/personen/) v2.7.0 (`POST /personen`).

### Configuration

| Property | Description |
|---|---|
| `brpPersonenBaseUrl` | Base url of the BRP API, without `/personen` (e.g. `https://proefomgeving.haalcentraal.nl/haalcentraal/api/brp`) |
| `authenticationPluginConfiguration` | A `haal-centraal-auth` plugin configuration |

### Actions

All actions can be used on a service task. The resulting list of persons is stored in the process variable `resultProcessVariableName`. An empty search result gives an empty list.

| Action | Required properties | Optional properties |
|---|---|---|
| `hc-brp-personen-raadpleeg-bsn` | `burgerservicenummer` (comma separated, max. 20) | `gemeenteVanInschrijving` |
| `hc-brp-personen-zoek-geslachtsnaam-geboortedatum` | `geslachtsnaam`, `geboortedatum` | `geslacht`, `voorvoegsel`, `voornamen`, `inclusiefOverledenPersonen`, `gemeenteVanInschrijving` |
| `hc-brp-personen-zoek-naam-gemeente` | `geslachtsnaam`, `voornamen`, `gemeenteVanInschrijving` | `geslacht`, `voorvoegsel`, `inclusiefOverledenPersonen` |
| `hc-brp-personen-zoek-postcode-huisnummer` | `postcode`, `huisnummer` | `huisletter`, `huisnummertoevoeging`, `geboortedatum`, `geslachtsnaam`, `inclusiefOverledenPersonen`, `gemeenteVanInschrijving` |
| `hc-brp-personen-zoek-straat-huisnummer-gemeente` | `straat`, `huisnummer`, `gemeenteVanInschrijving` | `huisletter`, `huisnummertoevoeging`, `inclusiefOverledenPersonen` |
| `hc-brp-personen-zoek-nummeraanduiding` | `nummeraanduidingIdentificatie` | `inclusiefOverledenPersonen`, `gemeenteVanInschrijving` |
| `hc-brp-personen-zoek-adresseerbaar-object` | `adresseerbaarObjectIdentificatie` | `inclusiefOverledenPersonen`, `gemeenteVanInschrijving` |

Every action also requires:

- `fields`: comma separated list of the fields to return, e.g. `burgerservicenummer,naam,geboorte,verblijfplaats`. Only request fields you need and are authorised for. Use the [fields tool](https://developer.rvig.nl/brp-api/fields-samenstellen/) to compose them.
- `resultProcessVariableName`: name of the process variable to store the result in.

All properties support value resolvers such as `doc:/bsn` or `pv:bsn`.

### Errors

Invalid parameters or `fields` result in a `400 Bad Request` from the BRP API. The service task fails and an incident is created.

### Local development

`backend/app/docker-compose.yml` starts `ghcr.io/brp-api/personen-mock` on port `5011`. The sandbox case `haal-centraal` contains the process *Haal Centraal Raadpleeg Personen met BSN*, which stores the result in `doc:/results` (shown on the summary tab). Test BSN: `999993653`.
