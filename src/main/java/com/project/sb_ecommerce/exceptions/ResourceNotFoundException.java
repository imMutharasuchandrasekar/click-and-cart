package com.project.sb_ecommerce.exceptions;

public class ResourceNotFoundException extends RuntimeException
{
    private String _resourceName;
    private String _field;
    private String _fieldName;
    private long _fieldId;

    public ResourceNotFoundException()
    {
    }

    public ResourceNotFoundException( String resourceName, String field, long fieldId )
    {
        super( String.format( "%s not found with %s: %s", resourceName, field, fieldId) );
        _resourceName = resourceName;
        _field = field;
        _fieldId = fieldId;
    }

    public ResourceNotFoundException( String resourceName, String field, String fieldName  )
    {
        super( String.format( "%s not found with %s: %s", resourceName, field, fieldName ) );
        _resourceName = resourceName;
        _field = field;
        _fieldName = fieldName;
    }
}
