package hu.bme.aut.fna1a3.shoppinglist2.model

data class Money (val date: String?, val rates: Rates?, val base: String?)

data class Rates(val BGN: Double?, val CAD: Double?, val BRL: Double?, val HUF: Double?, val DKK: Double?, val JPY: Double?, val ILS: Double?, val TRY: Double?, val RON: Double?, val GBP: Double?, val PHP: Double?, val HRK: Double?, val NOK: Double?, val USD: Double?, val MXN: Double?, val AUD: Double?, val IDR: Double?, val KRW: Double?, val HKD: Double?, val ZAR: Double?, val ISK: Double?, val CZK: Double?, val THB: Double?, val MYR: Double?, val NZD: Double?, val PLN: Double?, val SEK: Double?, val RUB: Double?, val CNY: Double?, val SGD: Double?, val CHF: Double?, val INR: Double?)