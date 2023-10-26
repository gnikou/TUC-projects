function [ blobMarkers ] = retrieveBlobMarkers( scale_space, radiusByScale, levels)
   
    blobMarkers = [];
    for i = 1:levels
        
        [newMarkerRows, newMarkerCols] = find(scale_space(:,:,i));
        
        newMarkers = [newMarkerCols'; newMarkerRows'];
        newMarkers(3,:) = radiusByScale(i);
        
        blobMarkers = [blobMarkers; newMarkers'];        
    end

end