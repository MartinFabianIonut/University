import { useEffect, useState } from 'react';
import { Network, ConnectionStatus } from '@capacitor/network';
import { PluginListenerHandle } from '@capacitor/core';
import { getLogger } from '../core';

const initialState = {
  connected: false,
  connectionType: 'unknown',
}

const log = getLogger('useNetwork:');

export const useNetwork = () => {
  const [networkStatus, setNetworkStatus] = useState(initialState)
  useEffect(() => {
    let handler: PluginListenerHandle;
    registerNetworkStatusChange();
    Network.getStatus().then(handleNetworkStatusChange);
    let canceled = false;
    return () => {
      canceled = true;
      handler?.remove();
    }

    async function registerNetworkStatusChange() {
      handler = await Network.addListener('networkStatusChange', handleNetworkStatusChange);
    }

    async function handleNetworkStatusChange(status: ConnectionStatus) {
      log('useNetwork - status change', status);
      if (!canceled) {
        setNetworkStatus(status);
      }
    }
  }, [])
  return { networkStatus };
};
