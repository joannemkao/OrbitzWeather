Author: Joanne Kao (joanne.kao@orbitz.com)

Comments and assumptions:

1.  My registration key is only effective with version 2 of the free API, which has a different format than version 1.
    Temperature, description, precipitation, icon are only available at the hourly level, not date level.
    Due to this change, have simplied the process to display the attributes from the first entry of the hourly JSONArray
    I set the hour param to 24 hours to get the daily average from the API.
    Documentation: http://www.worldweatheronline.com/api/docs/local-city-town-weather-api.aspx

2.  All temperatures are displayed in only in degrees Fahreinheit

3.  For simplicity, I am assuming there is only one element within the
    current_conditions { temperature, description[], precip, image[], ... }JSON array while parsing.
    If for any reason the API added more than one element, only the first would be taken.
