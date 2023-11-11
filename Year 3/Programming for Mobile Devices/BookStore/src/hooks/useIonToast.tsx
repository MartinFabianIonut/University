import { useState, useCallback, useMemo } from 'react';
import { IonToast } from '@ionic/react';
import { useContext } from 'react';
import { AuthContext } from '../providers/AuthProvider';

interface IonToastMessage {
    message: string;
}

const getErrorMessage = (error: any): string => {
    if (error.response && error.response.data && error.response.data.error) {
        return error.response.data.error;
    }
    if (error.response && error.response.data && error.response.data.message) {
        return error.response.data.message;
    }
    return error.message || 'An error occurred';
};

export const useIonToast = () => {
    const [toastMessage, setToastMessage] = useState<IonToastMessage | null>(null);
    const { logout } = useContext(AuthContext);

    const showToast = useCallback((m: IonToastMessage) => {
        setToastMessage(m);
        if (m.message.toLowerCase().includes('log in again')) {
            setTimeout(() => {
                logout?.();
            }, 3050);
        }
    }, []);

    const ToastComponent = useMemo(() => (
        <IonToast
            isOpen={!!toastMessage}
            onDidDismiss={() => setToastMessage(null)}
            message={toastMessage?.message || ''}
            duration={3000}
            position={'middle'}
            cssClass={'custom-toast'}
        />
    ), [toastMessage]);

    return { showToast, ToastComponent, getErrorMessage };
};
