import React from 'react';

const InfoBox = ({ major, minor }) => {
  return (
            <div className="inline info-box text-xs-center"><h2>{major}</h2><p>{minor}</p></div>
  );
};

export default InfoBox;
