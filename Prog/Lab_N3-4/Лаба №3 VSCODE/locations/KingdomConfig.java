package locations;

public record KingdomConfig ( int maxResidents,
                              boolean changableDescription,
                              boolean allowDuplicates,
                              String defaultLocationName
                            ) {}
