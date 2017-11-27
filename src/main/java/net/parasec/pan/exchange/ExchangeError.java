package net.parasec.pan.exchange;

public enum ExchangeError {
	CONNECTION_ERROR,     // something wrong with connection
	UNEXPECTED_RESPONSE,  // exchange sends us garbage
	MALFORMED_REQUEST,    // exchange did not understand our request
	AUTHENTICATION_ERROR, // could not authenticate for some reason
	HOLDBACK,             // we triggered dos controls (self or exchange trigger)
	INVALID_ID,           // if referencing an id unknown to the exchange
	INSUFFICIENT_FUNDS,   // we need more money 
	NOT_SUPPORTED,	      // not implemented yet
	UNKNOWN               // something we have not thought of
}
