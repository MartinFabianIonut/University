import React, { useContext } from 'react';
import { IonToolbar, IonTitle, IonButton } from '@ionic/react';
import CircleSymbol from './CircleSymbol';
import { useNetwork } from '../hooks/useNetwork';
import { AuthContext } from '../providers/AuthProvider';

interface CustomToolbarProps {
    title: string;
    titleStyle?: string;
}

const CustomToolbar: React.FC<CustomToolbarProps> = ({ title, titleStyle }) => {
    const { logout } = useContext(AuthContext);
    const { networkStatus } = useNetwork();

    const containerStyle: React.CSSProperties = {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: '99%',
    };

    const rightContentStyle: React.CSSProperties = {
        display: 'flex',
        alignItems: 'center',
    };

    return (
        <IonToolbar>
            <div style={containerStyle}>
                <IonTitle className={titleStyle}>{title}</IonTitle>
                <div style={rightContentStyle}>
                    <CircleSymbol status={networkStatus.connected} />
                    <IonButton shape='round' onClick={logout} >Logout</IonButton>
                </div>
            </div>
        </IonToolbar >
    );
};

export default CustomToolbar;
