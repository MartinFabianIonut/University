import { useEffect, useState } from 'react';
import { Geolocation, Position } from '@capacitor/geolocation';

interface MyLocation {
  position?: Position | null;
  error?: Error;
}

export const useMyLocation = () => {
  const [state, setState] = useState<MyLocation>({});
  useEffect(() => {
    watchMyLocation()
  }, []);
  return state;

  async function watchMyLocation() {
    let cancelled = false;
    let callbackId: string;

    const retryDelay = 200; 

    async function getCurrentPositionWithRetry() {
      try {
        return await Geolocation.getCurrentPosition();
      } catch (error) {
        if (error) {
          await new Promise(resolve => setTimeout(resolve, retryDelay));
          return getCurrentPositionWithRetry();
        }
        throw error;
      }
    }

    try {
      const position = await getCurrentPositionWithRetry();
      updateMyPosition('current', position);
    } catch (error) {
      updateMyPosition('current', null, error);
    }

    callbackId = await Geolocation.watchPosition({ timeout: 50 }, (position, error) => {
      if (error) {
        // Retry if it's a timeout error
        setTimeout(() => {
          watchMyLocation();
        }, retryDelay);
      } else {
        updateMyPosition('watch', position, error);
      }
    });

    return () => {
      cancelled = true;
      Geolocation.clearWatch({ id: callbackId });
    };

    function updateMyPosition(source: string, position?: Position | null, error: any = undefined) {
      if (!cancelled && (position || error)) {
        setState({ position, error });
      }
    }
  }
};
