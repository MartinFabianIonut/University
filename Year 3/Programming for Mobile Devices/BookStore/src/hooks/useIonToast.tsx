import React, { useState, useCallback, useMemo } from 'react';
import { IonToast, createAnimation } from '@ionic/react';
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
            }, 1550);
        }
    }, []);

    // Custom enter animation for the toast
    const enterAnimation = (baseEl: any) => {
        const root = baseEl.shadowRoot;
        const backdropAnimation = createAnimation()
            .addElement(root.querySelector('ion-backdrop')!)
            .fromTo('opacity', '0.01', 'var(--backdrop-opacity)');

        const wrapperAnimation = createAnimation()
            .addElement(root.querySelector('.toast-wrapper')!)
            .keyframes([
                { offset: 0, opacity: '0', transform: 'scale(0)' },
                { offset: 1, opacity: '0.99', transform: 'scale(1)' }
            ]);

        return createAnimation()
            .addElement(baseEl)
            .easing('ease-out')
            .duration(500)
            .addAnimation([backdropAnimation, wrapperAnimation]);
    }

    const leaveAnimation = (baseEl: any) => {
        return enterAnimation(baseEl).direction('reverse');
    }

    const ToastComponent = useMemo(() => (
        <IonToast
            isOpen={!!toastMessage}
            onDidDismiss={() => setToastMessage(null)}
            message={toastMessage?.message || ''}
            duration={1000}
            position={'middle'}
            cssClass={'custom-toast'}
            enterAnimation={enterAnimation}
            leaveAnimation={leaveAnimation}
        />
    ), [toastMessage]);

    return { showToast, ToastComponent, getErrorMessage };
};
