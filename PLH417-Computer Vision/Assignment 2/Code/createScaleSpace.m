function [scale_space] = createScaleSpace( img, levels, factor, sigma)

    dim = size(img);
    scale_space = zeros(dim(1),dim(2),levels);
    
    filter = fspecial('log', [5,5], sigma); %we create a 5x5 LoG filter
    
    filter = filter * sigma.^2 ;

    for i = 1:levels
    
        downsampled = imresize(img, 1/(factor^(i-1)), 'bicubic');
        
        filterImage = imfilter(downsampled, filter, 'replicate');
        
        filterImage = filterImage.^2;
        
        upscaled = imresize(filterImage, dim , 'bicubic');
        
        scale_space(:,:,i) = upscaled;
    end   
    
end