import { GoogleMap } from '@capacitor/google-maps';
import { useEffect, useRef } from 'react';
import { getLogger } from '../core';
import { MarkerCallbackData } from '@capacitor/google-maps/dist/typings/definitions';

const mapsApiKey = process.env.MAPS_API_KEY as string;
const mapId = process.env.MAP_ID as string;

const log = getLogger('MyMap');

interface MyMapProps {
  lat: number;
  lng: number;
  onMapClick: (e: any) => void;
}

const MyMap: React.FC<MyMapProps> = ({ lat, lng, onMapClick }) => {
  const mapRef = useRef<HTMLElement>(null);
  useEffect(myMapEffect, [mapRef.current])

  return (
    <div className="component-wrapper">
      <capacitor-google-map
        ref={mapRef}
        style={{
          display: 'block',
          width: '85%', // Set width to 85%
          height: '45vh', // Set height to 85% of the viewport height
          margin: '25px auto', // Center the map
          borderRadius: '15px', // Add rounded corners
        }}
      ></capacitor-google-map>
    </div>
  );

  function myMapEffect() {
    let canceled = false;
    let googleMap: GoogleMap | null = null;
    let listOfMarkers: string[] = [];
    createMap();
    return () => {
      canceled = true;
      googleMap?.removeAllMapListeners();
    }

    async function createMap() {
      try {
        if (!mapRef.current) {
          return;
        }
        googleMap = await GoogleMap.create({
          id: 'FabianMap',
          element: mapRef.current,
          apiKey: mapsApiKey,
          config: {
            mapId: mapId,
            center: { lat, lng },
            zoom: 14
          }
        });
        console.log('FabianMap was successfully created!');
        let marker = await googleMap.addMarker({ coordinate: { lat, lng }, title: 'My Marker' });
        listOfMarkers.push(marker);

        await googleMap.setOnMapClickListener(async ({ latitude, longitude }) => {
          onMapClick({ latitude, longitude });
          // change the marker position
          if (googleMap && listOfMarkers.length > 0) {
            googleMap?.removeMarkers(listOfMarkers);
            listOfMarkers = [];
            let newMarker = await googleMap?.addMarker({ coordinate: { lat: latitude, lng: longitude }, title: 'My Marker' });
            if (newMarker)
              listOfMarkers.push(newMarker);
          }
        });
        // put a marker on the map

      } catch (error) {
        log('createMap error', error);
      }
    }
  }
}

export default MyMap;
