import React from 'react';

interface CircleSymbolProps {
    status: boolean | null;
}

const CircleSymbol: React.FC<CircleSymbolProps> = ({ status }) => {
    const circleStyle: React.CSSProperties = {
        width: '35px',
        height: '35px',
        borderRadius: '50%',
        backgroundColor: status ? 'var(--ion-color-success-tint)' : 'var(--ion-color-danger-tint)',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        color: 'white',
        marginRight: '10px',
    };

    return (
        <div style={circleStyle}>
            <span>{status ? 'On' : 'Off'}</span>
        </div>
    );
};

export default CircleSymbol;
