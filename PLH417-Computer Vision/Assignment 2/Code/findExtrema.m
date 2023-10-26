function [ scaleSpace ] = findExtrema( local_extrema, scale_space, levels )

maxVals_InNeighboringScaleSpace = local_extrema;
for i = 1:levels
    if i == 1
        lowerScale = i;
        upperScale = i+1;
    elseif i < levels
        lowerScale = i-1;
        upperScale = i+1;
    else
        lowerScale = i-1;
        upperScale = i;
    end
    
    maxVals_InNeighboringScaleSpace(:,:,i) = max(maxVals_InNeighboringScaleSpace(:,:,lowerScale:upperScale),[],3);
end


originalValMarkers = maxVals_InNeighboringScaleSpace == scale_space;

scaleSpace = maxVals_InNeighboringScaleSpace .* originalValMarkers;

end