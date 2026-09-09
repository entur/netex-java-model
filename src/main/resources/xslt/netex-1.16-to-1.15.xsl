<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
  the European Commission - subsequent versions of the EUPL (the "Licence");
  You may not use this work except in compliance with the Licence.
  You may obtain a copy of the Licence at:

    https://joinup.ec.europa.eu/software/page/eupl

  Unless required by applicable law or agreed to in writing, software
  distributed under the Licence is distributed on an "AS IS" basis,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the Licence for the specific language governing permissions and
  limitations under the Licence.
-->
<!--
  Converts a NeTEx 1.16 PublicationDelivery into a NeTEx 1.15 PublicationDelivery.

  NeTEx 1.16 changed DatedServiceJourney in a way that is not backward compatible:
    * the journey reference is no longer repeatable; replaced journeys moved from
      additional DatedServiceJourneyRef elements into a replacedJourneys container
      holding DatedVehicleJourneyRef / NormalDatedVehicleJourneyRef elements;
    * DatedServiceJourneyRef does not exist any more in the schema;
    * OperatingDayRef and UicOperatingPeriod are no longer mutually exclusive.

  This stylesheet reverses those changes and rewrites the version attribute of the
  PublicationDelivery element. Everything else is copied unchanged.

  Only XSLT 1.0 features are used so the stylesheet runs on the JDK built-in engine
  as well as on libxslt (xmlstarlet tr netex-1.16-to-1.15.xsl input.xml).
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:netex="http://www.netex.org.uk/netex"
                xmlns="http://www.netex.org.uk/netex"
                exclude-result-prefixes="netex">

  <xsl:output method="xml" encoding="UTF-8"/>

  <!-- Version prefix to replace in PublicationDelivery/@version (e.g. "1.16:NO-NeTEx-networktimetable:1.3") -->
  <xsl:param name="sourceVersion" select="'1.16'"/>
  <!-- Version prefix written in its place -->
  <xsl:param name="targetVersion" select="'1.15'"/>

  <!-- Identity transform -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <!-- PublicationDelivery/@version: "1.16" or "1.16:profile" -> "1.15" or "1.15:profile" -->
  <xsl:template match="/netex:PublicationDelivery/@version">
    <xsl:attribute name="version">
      <xsl:choose>
        <xsl:when test="starts-with(., $sourceVersion)">
          <xsl:value-of select="concat($targetVersion, substring(., string-length($sourceVersion) + 1))"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="."/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:template>

  <!--
    DatedServiceJourney that needs rewriting (has replacedJourneys, or both OperatingDayRef and
    UicOperatingPeriod): rebuild the tail of the element in the 1.15 order
      JourneyRef*, (OperatingDayRef | UicOperatingPeriod)?, ExternalDatedVehicleJourneyRef?,
      DatedJourneyPatternRef?, DriverRef?
    The leading part of the element (ServiceAlteration, ServiceJourneyRef, ...) is identical
    in both versions and is copied in document order. Formatting whitespace inside the element
    is dropped because the children are reordered. Other DatedServiceJourney elements are copied
    unchanged by the identity template.
  -->
  <xsl:template match="netex:DatedServiceJourney[netex:replacedJourneys
                                                 or (netex:OperatingDayRef and netex:UicOperatingPeriod)]">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates select="node()[not(self::netex:replacedJourneys
                                            or self::netex:OperatingDayRef
                                            or self::netex:UicOperatingPeriod
                                            or self::netex:ExternalDatedVehicleJourneyRef
                                            or self::netex:DatedJourneyPatternRef
                                            or self::netex:DriverRef
                                            or (self::text() and normalize-space(.) = ''))]"/>
      <xsl:apply-templates select="netex:replacedJourneys/*"/>
      <xsl:choose>
        <xsl:when test="netex:OperatingDayRef">
          <xsl:apply-templates select="netex:OperatingDayRef"/>
        </xsl:when>
        <xsl:otherwise>
          <!-- UicOperatingPeriod is deprecated in 1.16; only kept when there is no OperatingDayRef -->
          <xsl:apply-templates select="netex:UicOperatingPeriod"/>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates select="netex:ExternalDatedVehicleJourneyRef"/>
      <xsl:apply-templates select="netex:DatedJourneyPatternRef"/>
      <xsl:apply-templates select="netex:DriverRef"/>
    </xsl:copy>
  </xsl:template>

  <!-- replacedJourneys/DatedVehicleJourneyRef and NormalDatedVehicleJourneyRef -> DatedServiceJourneyRef -->
  <xsl:template match="netex:DatedServiceJourney/netex:replacedJourneys/*">
    <DatedServiceJourneyRef>
      <xsl:apply-templates select="@*|node()"/>
    </DatedServiceJourneyRef>
  </xsl:template>

</xsl:stylesheet>
