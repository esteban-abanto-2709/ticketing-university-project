package ticketing.event.zone;

import ticketing.utils.ConsoleFormatter;

import java.util.List;

public class ZoneController {

    private final ZoneDAO zoneDAO = new ZoneDAO();

    public boolean register(Zone zone) {
        if (zone == null || zone.getEventCode() == null || zone.getName() == null) {
            ConsoleFormatter.printError("[ZoneController] Datos inválidos.");
            return false;
        }

        String eventCode = normalizeCode(zone.getEventCode());
        String name = normalizeName(zone.getName());

        if (exists(eventCode, name)) {
            ConsoleFormatter.printWarning("Ya existe una zona con ese nombre para este evento.");
            return false;
        }

        Zone toSave = new Zone(
                eventCode,
                name,
                zone.getCapacity(),
                zone.getBasePrice()
        );

        return zoneDAO.save(toSave);
    }

    public Zone findZone(String eventCode, String name) {
        if (eventCode == null || name == null) return null;
        return zoneDAO.findZone(normalizeCode(eventCode), normalizeName(name));
    }

    public List<Zone> findByEvent(String eventCode) {
        if (eventCode == null) return null;
        return zoneDAO.findByEventCode(normalizeCode(eventCode));
    }

    public boolean update(Zone zone) {
        if (zone == null || zone.getEventCode() == null || zone.getName() == null) {
            ConsoleFormatter.printError("[ZoneController] Datos inválidos para actualizar zona.");
            return false;
        }

        Zone toUpdate = new Zone(
                normalizeCode(zone.getEventCode()),
                normalizeName(zone.getName()),
                zone.getCapacity(),
                zone.getBasePrice()
        );

        return zoneDAO.update(toUpdate);
    }

    public boolean deleteZone(String eventCode, String name) {
        if (eventCode == null || name == null) return false;
        return zoneDAO.deleteZone(normalizeCode(eventCode), normalizeName(name));
    }

    public boolean deleteAllByEvent(String eventCode) {
        if (eventCode == null) return false;
        return zoneDAO.deleteAllByEvent(normalizeCode(eventCode));
    }

    public boolean exists(String eventCode, String name) {
        return findZone(eventCode, name) != null;
    }

    public boolean hasZones(String eventCode) {
        List<Zone> list = findByEvent(eventCode);
        return list != null && !list.isEmpty();
    }

    public int getTotalCapacity(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) return 0;
        return zoneDAO.getTotalCapacity(normalizeCode(eventCode));
    }

    private String normalizeCode(String code) {
        return (code == null) ? null : code.trim().toUpperCase();
    }

    private String normalizeName(String name) {
        return (name == null) ? null : name.trim();
    }
}
