import { useEffect, useState } from 'react';
import { Network, ConnectionStatus } from '@capacitor/network';
import { PluginListenerHandle } from '@capacitor/core';
import { getLogger } from '../core';

const initialState = {
  connected: null as boolean | null,
  connectionType: 'unknown',
  shouldSync: 'unknown' as string,
};

const log = getLogger('useNetwork:');

export const useNetwork = () => {
  const [networkStatus, setNetworkStatus] = useState(initialState);

  useEffect(() => {
    let handler: PluginListenerHandle;

    const initializeNetworkStatus = async () => {
      const initialStatus = await Network.getStatus();
      handleNetworkStatusChange(initialStatus);
    };

    registerNetworkStatusChange();
    initializeNetworkStatus();

    let canceled = false;
    return () => {
      canceled = true;
      handler?.remove();
    };

    async function registerNetworkStatusChange() {
      handler = await Network.addListener('networkStatusChange', handleNetworkStatusChange);
    }

    function handleNetworkStatusChange(status: ConnectionStatus) {
      log('useNetwork - status change', status);
      if (!canceled) {
        setNetworkStatus((prevStatus) => ({
          connected: status.connected,
          connectionType: status.connectionType,
          shouldSync: prevStatus.connected === null ? 'do-not-sync' : prevStatus.connected === true ? 'connected' : 'disconnected',
        }));
      }
    }
  }, []);

  return { networkStatus };
};
