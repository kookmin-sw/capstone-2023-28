package com.capstone.traffic.model.network.sk.direction.dataClass

object testData {
    val data = objects(
        metaData(
            requestParameters(
                busCount = "1",
                subwayCount = "1",
                subwayBusCount = "1"
            ),
            plan(
                listOf(
                    itineraries(
                      fare = fare(
                          regular = regular(
                              totalFare = "2000",
                              currency = currency(
                                  symbol = "원",
                                  currency = "원"
                              )
                          )
                      ),
                        totalTime = "2000",
                        legs = listOf(
                            legs(
                                mode = "walk",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),
                            legs(
                                mode = "bus",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),legs(
                                mode = "walk",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),
                        )
                    ),
                    itineraries(
                        fare = fare(
                            regular = regular(
                                totalFare = "2000",
                                currency = currency(
                                    symbol = "원",
                                    currency = "원"
                                )
                            )
                        ),
                        totalTime = "2000",
                        legs = listOf(
                            legs(
                                mode = "walk",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),
                            legs(
                                mode = "bus",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),legs(
                                mode = "subway",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),
                        )
                    ),
                    itineraries(
                        fare = fare(
                            regular = regular(
                                totalFare = "2000",
                                currency = currency(
                                    symbol = "원",
                                    currency = "원"
                                )
                            )
                        ),
                        totalTime = "2000",
                        legs = listOf(
                            legs(
                                mode = "walk",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),
                            legs(
                                mode = "subway",
                                sectionTime = "500",
                                start = name(
                                    name = "시작"
                                ),
                                end = name(
                                    name = "끝"
                                ),
                                route = "100번버스"
                            ),
                        )
                    ),
                )
            )
        )
    )
}